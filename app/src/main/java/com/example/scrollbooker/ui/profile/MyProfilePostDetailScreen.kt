package com.example.scrollbooker.ui.profile
import androidx.annotation.OptIn
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.media3.common.util.UnstableApi
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.ui.shared.posts.PostActionUiState
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostOverlay
import com.example.scrollbooker.ui.theme.BackgroundDark
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.scrollbooker.core.extensions.getOrNull
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.shared.posts.components.PostPlayerView
import com.example.scrollbooker.ui.shared.posts.components.PostShimmer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(UnstableApi::class)
@Composable
fun MyProfilePostDetailScreen(
    viewModel: MyProfileViewModel,
    posts: LazyPagingItems<Post>,
    onBack: () -> Unit
) {
    val selectedPost by viewModel.selectedPost.collectAsState()
    val currentPost by viewModel.currentPost.collectAsState()
    val pagerState = rememberPagerState(initialPage = 0) { posts.itemCount }

    LaunchedEffect(selectedPost?.postId) {
        val targetId = selectedPost?.postId ?: return@LaunchedEffect

        repeat(20) {
            val idx = posts.itemSnapshotList.items.indexOfFirst { it.id == targetId }
            if (idx >= 0) {
                pagerState.scrollToPage(idx)
                return@LaunchedEffect
            }
            delay(100)
        }
    }

    val title: String = when(selectedPost?.tab) {
        PostTabEnum.MY_POSTS -> stringResource(R.string.posts)
        PostTabEnum.REPOSTS -> stringResource(R.string.repost)
        PostTabEnum.BOOKMARKS -> stringResource(R.string.bookmarks)
        null -> ""
    }

    val currentOnReleasePlayer by rememberUpdatedState(viewModel::stopDetailSession)

    LifecycleStartEffect(true) {
        onStopOrDispose {
            currentOnReleasePlayer()
        }
    }

    LaunchedEffect(pagerState.settledPage) {
        snapshotFlow { pagerState.settledPage }
            .distinctUntilChanged()
            .collectLatest { page ->
                val post = posts.getOrNull(page)

                viewModel.onPageSettled(page, post)
                viewModel.ensureWindow(
                    centerIndex = page,
                    getPost = { idx -> posts.getOrNull(idx) }
                )
            }
    }

    val decay = rememberSplineBasedDecay<Float>()

    val snapSpec: SpringSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessHigh,
    )

    val fling = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(1),
        decayAnimationSpec = decay,
        snapAnimationSpec = snapSpec
    )

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            Header(
                modifier = Modifier.zIndex(14f),
                onBack = onBack,
                title = title,
                icon = Icons.Default.Close,
                iconSize = 30.dp,
                withBackground = false
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            VerticalPager(
                state = pagerState,
                overscrollEffect = null,
                flingBehavior = fling,
                pageSize = PageSize.Fill,
                pageSpacing = 0.dp,
                beyondViewportPageCount = 1,
                modifier = Modifier.weight(1f),
            ) { page ->
                val post = posts.getOrNull(page) ?: return@VerticalPager
                val player = viewModel.getPlayerForIndex(page)

                Box(modifier = Modifier.fillMaxSize()) {
                    if(player != null) {
                        PostPlayerWithThumbnail(
                            player = player,
                            thumbnailUrl = post.mediaFiles.first().thumbnailUrl
                        )
                    } else {
                        AsyncImage(
                            modifier = Modifier.fillMaxSize(),
                            model = post.mediaFiles.first().thumbnailUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }

                    PostOverlay(
                        post = post,
                        postActionState = PostActionUiState(),
                        onAction = {},
                        enableOpacity = false,
                        showBottomBar = false,
                        onShowBottomBar = null,
                        onNavigateToUserProfile = {}
                    )
                }
            }

            MainButton(
                modifier = Modifier
                    .padding(
                        vertical = SpacingS,
                        horizontal = BasePadding
                    ),
                contentPadding = PaddingValues(14.dp),
                onClick = {},
                title = "Rezervă instant",
            )
        }
    }
}

@Composable
fun PostPlayerWithThumbnail(
    player: ExoPlayer,
    thumbnailUrl: String
) {
    var showThumb by remember(thumbnailUrl) { mutableStateOf(true) }
    var isBuffering by remember { mutableStateOf(false) }

    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                isBuffering = playbackState == Player.STATE_BUFFERING
            }

            override fun onRenderedFirstFrame() {
                showThumb = false
                isBuffering = false
            }
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                showThumb = true
                isBuffering = true
            }

            override fun onPlayerError(error: PlaybackException) {
                showThumb = true
                isBuffering = true
            }


        }
        player.addListener(listener)
        onDispose { player.removeListener(listener) }
    }

    Box(Modifier.fillMaxSize()) {
        PostPlayerView(player)

        if(showThumb) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = thumbnailUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        if(isBuffering && !showThumb) {
            PostShimmer()
        }
    }
}