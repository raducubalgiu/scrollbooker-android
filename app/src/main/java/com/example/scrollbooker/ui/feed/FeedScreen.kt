package com.example.scrollbooker.ui.feed
import BottomBar
import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.navigation.bottomBar.MainTab
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.main.MainUIViewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView.SHOW_BUFFERING_NEVER
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.ui.feed.components.FeedTabs
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(UnstableApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FeedScreen(
    feedViewModel: FeedScreenViewModel,
    viewModel: MainUIViewModel,
    posts: LazyPagingItems<Post>,
    drawerState: DrawerState,
    appointmentsNumber: Int,
    onOpenDrawer: () -> Unit,
    onNavigateSearch: () -> Unit,
    onNavigate: (MainTab) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { posts.itemCount })
    val currentOnReleasePlayer by rememberUpdatedState(feedViewModel::releasePlayer)

    LifecycleStartEffect(true) {
        onStopOrDispose {
            currentOnReleasePlayer(posts[pagerState.currentPage]?.id)
        }
    }

    var shouldDisplayBottomBar by rememberSaveable { mutableStateOf(true) }

    Scaffold(
        bottomBar = {
            AnimatedContent(
                targetState = shouldDisplayBottomBar,
                transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
                label = "label"
            ) { display ->
                if(display) {
                    BottomBar(
                        appointmentsNumber = appointmentsNumber,
                        currentTab = MainTab.Feed,
                        currentRoute = MainRoute.Feed.route,
                        onNavigate = onNavigate
                    )
                } else {
                    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

                    MainButton(
                        modifier = Modifier
                            .padding(horizontal = BasePadding)
                            .padding(bottom = bottomPadding),
                        contentPadding = PaddingValues(SpacingM),
                        leadingIcon = R.drawable.ic_calendar_outline,
                        onClick = {},
                        title = "Intervale disponibile"
                    )
                }
            }
        },
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
        ) {
            FeedTabs(
                selectedTabIndex = 0,
                shouldDisplayBottomBar = shouldDisplayBottomBar,
                onChangeTab = {},
                onOpenDrawer = onOpenDrawer,
                onNavigateSearch = onNavigateSearch,
                onShowBottomBar = { shouldDisplayBottomBar = true }
            )

            posts.apply {
                when(loadState.refresh) {
                    is LoadState.Error -> ErrorScreen()
                    is LoadState.Loading -> Unit
                    is LoadState.NotLoading -> {

                        val postId by remember {
                            derivedStateOf { posts[pagerState.currentPage]?.id }
                        }

                        LaunchedEffect(Unit) {
                            snapshotFlow { drawerState.currentValue }
                                .collectLatest { drawerValue ->
                                    postId?.let {
                                        if(drawerValue == DrawerValue.Open) {
                                            feedViewModel.pauseIfPlaying(postId!!)
                                        } else {
                                            feedViewModel.resumeIfPlaying(postId!!)
                                        }
                                    }
                                }
                        }

                        LaunchedEffect(pagerState) {
                            snapshotFlow { pagerState.currentPage }
                                .debounce(150)
                                .collectLatest { page ->
                                val post = posts[page]
                                val previousPost = if(page > 1) posts[page - 1] else null
                                val nextPost = if(page < posts.itemCount - 1) posts[page + 1] else null

                                post?.let {
                                    feedViewModel.changePlayerItem(
                                        post = post,
                                        previousPost = previousPost,
                                        nextPost = nextPost
                                    )
                                    feedViewModel.pauseUnusedPlayers(visiblePostId = post.id)
                                }
                            }
                        }

                        LaunchedEffect(Unit) {
                            var previousPage = pagerState.currentPage
                            snapshotFlow { pagerState.currentPage }
                                .distinctUntilChanged()
                                .collect { currentPage ->
                                    val shouldShow = currentPage == 0 || currentPage < previousPage
                                    shouldDisplayBottomBar = shouldShow
                                    previousPage = currentPage
                                }
                        }

                        VerticalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 90.dp)
                        ) { page ->
                            val post = posts[page]

                            if(post != null) {
                                val player = remember(post.id) {
                                    feedViewModel.getOrCreatePlayer(post)
                                }
                                var isPlaying by remember { mutableStateOf(player.isPlaying) }
                                var isBuffering by remember { mutableStateOf(false) }
                                var isFirstFrameRendered by remember { mutableStateOf(false) }
                                var hasStartedPlayback by remember { mutableStateOf(false) }

                                DisposableEffect(player) {
                                    val listener = object : Player.Listener {
                                        override fun onIsPlayingChanged(playing: Boolean) {
                                            isPlaying = playing
                                            if(isPlaying) hasStartedPlayback = true
                                        }

                                        override fun onPlaybackStateChanged(state: Int) {
                                            isBuffering = state == Player.STATE_BUFFERING
                                        }

                                        override fun onRenderedFirstFrame() {
                                            isFirstFrameRendered = true
                                        }
                                    }

                                    player.addListener(listener)
                                    onDispose {
                                        player.removeListener(listener)
                                        isPlaying = false
                                        isBuffering = false
                                        isFirstFrameRendered = false
                                        hasStartedPlayback = false
                                    }
                                }

                                Box(modifier = Modifier
                                    .fillMaxSize()
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = { feedViewModel.togglePlayer(post.id) }
                                    )
                                ) {
                                    if(isBuffering) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(50.dp).align(Alignment.Center),
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
                                            playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                                        },
                                        modifier = Modifier.fillMaxSize(),
                                    )

                                    AnimatedVisibility(
                                        visible = hasStartedPlayback && !isPlaying && !isBuffering && !drawerState.isOpen,
                                        enter = fadeIn(),
                                        exit = fadeOut()
                                    ) {
                                        Box(modifier = Modifier
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

//                                    var progress by remember(post.id) { mutableFloatStateOf(0f) }
//
//                                    LaunchedEffect(player) {
//                                        while (true) {
//                                            val duration = player.duration.takeIf { it > 0 } ?: 1L
//                                            val position = player.currentPosition
//                                            progress = (position / duration.toFloat()).coerceIn(0f, 1f)
//                                            delay(100)
//                                        }
//                                    }
//
//                                    VideoSlider(
//                                        progress = progress,
//                                        isPlaying = isPlaying,
//                                        onSeek = {},
//                                        modifier = Modifier
//                                            .align(Alignment.BottomCenter)
//                                            .fillMaxWidth()
//                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

//    val pagerState = rememberPagerState(initialPage = 1) { 2 }
//    val selectedTabIndex = pagerState.currentPage
//    val coroutineScope = rememberCoroutineScope()
//
//    var isUserSwiping by remember { mutableStateOf(false) }
//    var shouldDisplayBottomBar by remember { mutableStateOf(true) }
//
//    LaunchedEffect(pagerState) {
//        snapshotFlow { pagerState.currentPageOffsetFraction }
//            .collect { offset ->
//                isUserSwiping = offset != 0f
//            }
//    }
//
//
//    Scaffold(
//        bottomBar = {
//            AnimatedContent(
//                targetState = shouldDisplayBottomBar,
//                transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
//                label = "label"
//            ) { display ->
//                if(display) {
//                    BottomBar(
//                        appointmentsNumber = appointmentsNumber,
//                        currentTab = MainTab.Feed,
//                        currentRoute = MainRoute.Feed.route,
//                        onNavigate = onNavigate
//                    )
//                } else {
//                    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
//
//                    MainButton(
//                        modifier = Modifier
//                            .padding(horizontal = BasePadding)
//                            .padding(bottom = bottomPadding),
//                        contentPadding = PaddingValues(SpacingM),
//                        leadingIcon = R.drawable.ic_calendar_outline,
//                        onClick = {},
//                        title = "Intervale disponibile"
//                    )
//                }
//            }
//        }
//    ) {
//        Box(modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFF121212))
//        ) {
//            FeedTabs(
//                selectedTabIndex = selectedTabIndex,
//                onOpenDrawer = onOpenDrawer,
//                onNavigateSearch = onNavigateSearch,
//                shouldDisplayBottomBar = shouldDisplayBottomBar,
//                onChangeTab = {
//                    coroutineScope.launch {
//                        pagerState.animateScrollToPage(it)
//                    }
//                }
//            )
//
//            HorizontalPager(
//                state = pagerState,
//                beyondViewportPageCount = 1,
//                pageSize = PageSize.Fill,
//                key = { it },
//                modifier = Modifier.fillMaxSize()
//            ) { page ->
//                when(page) {
//                    0 -> {
//                        val posts = viewModel.followingPosts.collectAsLazyPagingItems()
//
//                        PostsPager(
//                            posts = posts,
//                            isVisibleTab = pagerState.currentPage == page && !isUserSwiping,
//                            onDisplayBottomBar = { shouldDisplayBottomBar = it }
//                        )
//                    }
//                    1 -> {
//                        PostsPager(
//                            posts = bookNowPosts,
//                            isVisibleTab = pagerState.currentPage == page && !isUserSwiping,
//                            onDisplayBottomBar = { shouldDisplayBottomBar = it }
//                        )
//                    }
//                }
//            }
//        }
//    }
}
//
//@SuppressLint("RememberReturnType")
//@OptIn(UnstableApi::class)
//@Composable
//fun FollowingPager(
//    modifier: Modifier = Modifier,
//    viewModel: FeedScreenViewModel,
//    posts: LazyPagingItems<Post>,
//    //player: Player?,
//    onReleasePlayer: () -> Unit,
//    onChangePlayerItem: (post: Post) -> Unit
//) {
//
//}

