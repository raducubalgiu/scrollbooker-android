package com.example.scrollbooker.ui.feed
import BottomBar
import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.ui.PlayerView
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.navigation.bottomBar.MainTab
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.main.MainUIViewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout

@OptIn(UnstableApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FeedScreen(
    viewModel: MainUIViewModel,
    appointmentsNumber: Int,
    onOpenDrawer: () -> Unit,
    onNavigateSearch: () -> Unit,
    onNavigate: (MainTab) -> Unit
) {
    val feedViewModel: FeedScreenViewModel = hiltViewModel()
    val posts = feedViewModel.followingPosts.collectAsLazyPagingItems()

    Scaffold(
        bottomBar = {
            BottomBar(
                appointmentsNumber = appointmentsNumber,
                currentTab = MainTab.Feed,
                currentRoute = MainRoute.Feed.route,
                onNavigate = onNavigate
            )
        },
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
        ) {
            val pagerState = rememberPagerState(pageCount = { posts.itemCount })
            val currentOnReleasePlayer by rememberUpdatedState { feedViewModel.releasePlayer() }

            LifecycleStartEffect(true) {
                onStopOrDispose {
                    currentOnReleasePlayer()
                }
            }

            posts.apply {
                when(loadState.refresh) {
                    is LoadState.Error -> ErrorScreen()
                    is LoadState.Loading -> LoadingScreen()
                    is LoadState.NotLoading -> {

                        LaunchedEffect(pagerState) {
                            snapshotFlow { pagerState.currentPage }.collect { page ->
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

                                Box(modifier = Modifier.fillMaxSize()) {
                                    AndroidView(
                                        factory = { PlayerView(it) },
                                        update = { playerView ->
                                            playerView.player = player
                                            playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                                            playerView.useController = false
                                            playerView.layoutParams = ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                            )
                                        },
                                        modifier = Modifier.fillMaxSize(),
                                    )
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

