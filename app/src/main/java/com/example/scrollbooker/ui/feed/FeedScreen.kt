@file:kotlin.OptIn(FlowPreview::class)

package com.example.scrollbooker.ui.feed
import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.ui.PlayerView
import androidx.paging.LoadState
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.navigation.bottomBar.MainTab
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView.SHOW_BUFFERING_NEVER
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.getOrNull
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.navigators.FeedNavigator
import com.example.scrollbooker.navigation.navigators.NavigateCalendarParam
import com.example.scrollbooker.ui.feed.components.FeedTabs
import com.example.scrollbooker.ui.modules.posts.components.PostBottomBar
import com.example.scrollbooker.ui.modules.posts.components.postOverlay.PostOverlay
import com.example.scrollbooker.ui.theme.BackgroundDark
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(UnstableApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FeedScreen(
    feedViewModel: FeedScreenViewModel,
    posts: LazyPagingItems<Post>,
    drawerState: DrawerState,
    appointmentsNumber: Int,
    onOpenDrawer: () -> Unit,
    onChangeTab: (MainTab) -> Unit,
    feedNavigate: FeedNavigator
) {
    val pagerState = rememberPagerState(pageCount = { posts.itemCount })
    val currentOnReleasePlayer by rememberUpdatedState(feedViewModel::releasePlayer)

    LifecycleStartEffect(true) {
        onStopOrDispose {
            currentOnReleasePlayer(posts.getOrNull(pagerState.currentPage)?.id)
        }
    }

    var shouldDisplayBottomBar by rememberSaveable { mutableStateOf(true) }
    val currentPost by feedViewModel.currentPost.collectAsState()

    fun navigateToCalendar() {
        val userId = currentPost?.user?.id
        val slotDuration = currentPost?.product?.duration
        val productId = currentPost?.product?.id
        val productName = currentPost?.product?.name

        if(userId != null && slotDuration != null && productId != null && productName != null) {
            feedNavigate.toCalendar(
                NavigateCalendarParam(userId, slotDuration, productId, productName)
            )
        }
    }

    Scaffold(
        containerColor = BackgroundDark,
        bottomBar = {
            PostBottomBar(
                onAction = { navigateToCalendar() },
                shouldDisplayBottomBar = shouldDisplayBottomBar,
                appointmentsNumber = appointmentsNumber,
                onChangeTab = onChangeTab,
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            FeedTabs(
                selectedTabIndex = 0,
                onChangeTab = {},
                onOpenDrawer = onOpenDrawer,
                onNavigateSearch = {
                    feedNavigate.toFeedSearch()
                }
            )

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
                                            feedViewModel.pauseIfPlaying(it)
                                        } else {
                                            feedViewModel.resumeIfPlaying(it)
                                        }
                                    }
                                }
                        }

                        LaunchedEffect(pagerState) {
                            snapshotFlow { pagerState.currentPage }
                                .debounce(150)
                                .collectLatest { page ->
                                    val post = posts.getOrNull(page)
                                    val previousPost = posts.getOrNull(page - 1)
                                    val nextPost = posts.getOrNull(page + 1)

                                    post?.let {
                                        feedViewModel.initializePlayer(
                                            post = post,
                                            previousPost = previousPost,
                                            nextPost = nextPost
                                        )
                                        feedViewModel.pauseUnusedPlayers(visiblePostId = post.id)
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
                            pageSpacing = 0.dp
                        ) { page ->
                            val post = posts[page]

                            if (post != null) {
                                val player = remember(post.id) {
                                    feedViewModel.getOrCreatePlayer(post)
                                }
                                val playerState by feedViewModel.getPlayerState(post.id)
                                    .collectAsState()

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null,
                                            onClick = { feedViewModel.togglePlayer(post.id) }
                                        )
                                ) {
                                    if (playerState.isBuffering) {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .size(50.dp)
                                                .align(Alignment.Center),
                                            color = Color.White.copy(0.5f)
                                        )
                                    }

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
                                        onShowBottomBar = { shouldDisplayBottomBar = !shouldDisplayBottomBar },
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
    }
}

