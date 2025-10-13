package com.example.scrollbooker.ui.feed.tabs

import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.media3.ui.PlayerView.SHOW_BUFFERING_NEVER
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.getOrNull
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.navigators.FeedNavigator
import com.example.scrollbooker.ui.feed.PlayerViewModel
import com.example.scrollbooker.ui.modules.posts.components.postOverlay.PostOverlay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(UnstableApi::class)
@Composable
fun FollowingPostsTab(
    posts: LazyPagingItems<Post>,
    drawerState: DrawerState,
    shouldDisplayBottomBar: Boolean,
    onShowBottomBar: () -> Unit,
    feedNavigate: FeedNavigator
) {
    val playerViewModel: PlayerViewModel = hiltViewModel()
    val currentPost by playerViewModel.currentPost.collectAsState()

    val pagerState = rememberPagerState(pageCount = { posts.itemCount })
    val currentOnReleasePlayer by rememberUpdatedState(playerViewModel::releasePlayer)

    LifecycleStartEffect(true) {
        onStopOrDispose {
            currentOnReleasePlayer(posts.getOrNull(pagerState.currentPage)?.id)
        }
    }

    posts.apply {
        when (loadState.refresh) {
            is LoadState.Error -> ErrorScreen()
            is LoadState.Loading -> Unit
            is LoadState.NotLoading -> {
                val postId by remember {
                    derivedStateOf {
                        posts.getOrNull(pagerState.currentPage)?.id
                    }
                }

                LaunchedEffect(drawerState.currentValue) {
                    snapshotFlow { drawerState.currentValue }
                        .collectLatest { drawerValue ->
                            postId?.let {
                                if (drawerValue == DrawerValue.Open) {
                                    playerViewModel.pauseIfPlaying(it)
                                } else {
                                    playerViewModel.resumeIfPlaying(it)
                                }
                            }
                        }
                }

                LaunchedEffect(pagerState) {
                    snapshotFlow { pagerState.settledPage }
                        .debounce(150)
                        .collectLatest { page ->
                            val post = posts.getOrNull(page)
                            val previousPost = posts.getOrNull(page - 1)
                            val nextPost = posts.getOrNull(page + 1)

                            post?.let {
                                playerViewModel.initializePlayer(
                                    post = post,
                                    previousPost = previousPost,
                                    nextPost = nextPost
                                )
                                playerViewModel.pauseUnusedPlayers(visiblePostId = post.id)
                            }
                        }
                }

                VerticalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 90.dp),
                    overscrollEffect = null,
                    pageSize = PageSize.Fill,
                    pageSpacing = 0.dp,
                    beyondViewportPageCount = 1
                ) { page ->
                    val post = posts[page]

                    if (post != null) {
                        val player = remember(post.id) {
                            playerViewModel.getOrCreatePlayer(post)
                        }
                        val playerState by playerViewModel.getPlayerState(post.id)
                            .collectAsState()

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { playerViewModel.togglePlayer(post.id) }
                                )
                        ) {

                            AndroidView(
                                factory = { context ->
                                    PlayerView(context).apply {
                                        useController = false
                                        controllerAutoShow = false
                                        controllerShowTimeoutMs = 0

                                        setShowBuffering(SHOW_BUFFERING_NEVER)

                                        layoutParams = ViewGroup.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                        )
                                    }
                                },
                                update = { playerView ->
                                    playerView.player = player
                                    playerView.resizeMode =
                                        AspectRatioFrameLayout.RESIZE_MODE_FILL
                                },
                                modifier = Modifier.fillMaxSize(),
                            )

                            PostOverlay(
                                post = post,
                                onAction = {},
                                shouldDisplayBottomBar = shouldDisplayBottomBar,
                                onShowBottomBar = onShowBottomBar,
                                onNavigateToUserProfile = { feedNavigate.toUserProfile(it) },
                                onNavigateToCalendar = { feedNavigate.toCalendar(it) },
                                onNavigateToProducts = { feedNavigate.toUserProducts(userId = post.user.id) }
                            )

                            AnimatedVisibility(
                                visible = playerState.hasStartedPlayback &&
                                        !playerState.isPlaying &&
                                        !playerState.isBuffering &&
                                        !drawerState.isOpen,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .zIndex(5f),
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
                }
            }
        }
    }
}