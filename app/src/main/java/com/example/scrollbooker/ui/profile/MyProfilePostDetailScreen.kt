@file:kotlin.OptIn(ExperimentalMaterial3Api::class)

package com.example.scrollbooker.ui.profile
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.media3.common.util.UnstableApi
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostOverlay
import com.example.scrollbooker.ui.theme.BackgroundDark
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.paging.LoadState
import coil.compose.AsyncImage
import com.example.scrollbooker.core.extensions.getOrNull
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.social.post.data.mappers.applyUiState
import com.example.scrollbooker.ui.profile.components.PostTabEnum
import com.example.scrollbooker.ui.profile.components.ProfileLayoutViewModel
import com.example.scrollbooker.ui.shared.posts.components.PostPlayerView
import com.example.scrollbooker.ui.shared.posts.components.PostShimmer
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostOverlayActionEnum
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheets
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.BookingsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.CommentsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.LocationSheet
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.MoreOptionsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.None
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.PhoneSheet
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.ReviewsSheet
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyProfilePostDetailScreen(
    layoutViewModel: ProfileLayoutViewModel,
    posts: LazyPagingItems<Post>,
    onBack: () -> Unit,
    onNavigateToUserProfile: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    val selectedPost by layoutViewModel.selectedPost.collectAsState()
    val startIndex = selectedPost?.index ?: 0

    val title: String = when(selectedPost?.tab) {
        PostTabEnum.MY_POSTS -> stringResource(R.string.posts)
        PostTabEnum.BOOKMARKS -> stringResource(R.string.bookmarks)
        null -> ""
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var sheetContent by remember { mutableStateOf<PostSheetsContent>(None) }

    if(sheetState.isVisible) {
        key(sheetContent) {
            PostSheets(
                sheetState = sheetState,
                sheetContent = sheetContent,
                onClose = {
                    scope.launch {
                        sheetState.hide()
                        sheetContent = None
                    }
                },
            )
        }
    }

    fun handleOpenSheet(targetSheet: PostSheetsContent) {
        scope.launch {
            sheetState.show()
            sheetContent = targetSheet
        }
    }

    key(startIndex) {
        val pagerState = rememberPagerState(
            initialPage = startIndex
        ) { posts.itemCount }

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.settledPage }
                .distinctUntilChanged()
                .drop(1)
                .collectLatest { page ->
                    layoutViewModel.onPageSettled(page)
                    layoutViewModel.ensureWindow(
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

        val currentOnReleasePlayer by rememberUpdatedState(layoutViewModel::stopDetailSession)

        LifecycleStartEffect(true) {
            onStopOrDispose {
                currentOnReleasePlayer()
            }
        }

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
                    val player = layoutViewModel.getPlayerForIndex(page)

                    val postActionState by layoutViewModel
                        .observePostUi(post.id)
                        .collectAsStateWithLifecycle()

                    val postUi = remember(post, postActionState) {
                        post.copy(
                            userActions = post.userActions.applyUiState(postActionState),
                            counters = post.counters.applyUiState(postActionState)
                        )
                    }

                    Box(modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { layoutViewModel.togglePlayer(page) }
                        )
                    ) {
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
                            post = postUi,
                            isSavingLike = postActionState.isSavingLike,
                            isSavingBookmark = postActionState.isSavingBookmark,
                            onAction = { action ->
                                selectedPost?.let { post ->
                                    handlePostAction(
                                        feedViewModel = layoutViewModel,
                                        action = action,
                                        handleOpenSheet = { handleOpenSheet(it) },
                                        post = post.post
                                    )
                                }
                            },
                            onNavigateToUserProfile = {
                                if(it == post.user.id) {
                                    onBack()
                                } else {
                                    onNavigateToUserProfile(it)
                                }
                            }
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
                    onClick = {
                        selectedPost?.let { post ->
                            handlePostAction(
                                feedViewModel = layoutViewModel,
                                action = PostOverlayActionEnum.OPEN_BOOKINGS,
                                handleOpenSheet = { handleOpenSheet(it) },
                                post = post.post
                            )
                        }
                    },
                    title = stringResource(R.string.bookNow),
                )
            }
        }
    }
}

private fun handlePostAction(
    feedViewModel: ProfileLayoutViewModel,
    action: PostOverlayActionEnum,
    handleOpenSheet: (PostSheetsContent) -> Unit,
    post: Post
) {
    when(action) {
        PostOverlayActionEnum.OPEN_BOOKINGS -> handleOpenSheet(BookingsSheet(post.user))
        PostOverlayActionEnum.OPEN_REVIEWS -> {
            val id = if(post.isVideoReview) post.businessOwner.id else post.user.id
            handleOpenSheet(ReviewsSheet(id))
        }
        PostOverlayActionEnum.OPEN_COMMENTS -> handleOpenSheet(CommentsSheet(post.id))
        PostOverlayActionEnum.OPEN_LOCATION -> handleOpenSheet(LocationSheet(post.businessId))
        PostOverlayActionEnum.OPEN_MORE_OPTIONS -> handleOpenSheet(MoreOptionsSheet(post.user.id, post.isOwnPost))
        PostOverlayActionEnum.OPEN_PHONE -> handleOpenSheet(PhoneSheet(0.7f))
        PostOverlayActionEnum.LIKE -> feedViewModel.toggleLike(post)
        PostOverlayActionEnum.BOOKMARK -> feedViewModel.toggleBookmark(post)
    }
}

@Composable
fun PostPlayerWithThumbnail(
    player: ExoPlayer,
    thumbnailUrl: String,
    showPlayIcon: Boolean = false
) {
    var isRenderedFirstFrame by remember { mutableStateOf(false) }

    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onRenderedFirstFrame() {
                isRenderedFirstFrame = true
            }
        }
        player.addListener(listener)
        onDispose { player.removeListener(listener) }
    }

    Box(Modifier.fillMaxSize()) {
        PostPlayerView(player)

        if(!isRenderedFirstFrame) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = thumbnailUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        AnimatedVisibility(
            visible = showPlayIcon,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .zIndex(20f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(75.dp),
                    painter = painterResource(R.drawable.ic_play_solid),
                    contentDescription = null,
                    tint = Color.White.copy(0.5f)
                )
            }
        }
    }
}