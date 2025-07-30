package com.example.scrollbooker.ui.feed
import BottomBar
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.bottomBar.MainTab
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.feed.components.FeedTabs
import com.example.scrollbooker.ui.main.MainUIViewModel
import com.example.scrollbooker.ui.sharedModules.posts.PostsPager
import kotlinx.coroutines.launch
import kotlin.collections.count
import kotlin.math.absoluteValue
import androidx.core.net.toUri
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.sharedModules.posts.PostsPagerViewModel
import com.example.scrollbooker.ui.sharedModules.posts.components.postOverlay.PostOverlay
import com.example.scrollbooker.ui.sharedModules.posts.components.postOverlay.PostOverlayActionEnum

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
    val player = feedViewModel.player

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
        Box(Modifier.fillMaxSize()) {
            FollowingPager(
                modifier = Modifier,
                posts = posts,
                player = player,
                onInitializePlayer = feedViewModel::initializePlayer,
                onReleasePlayer = feedViewModel::releasePlayer,
                onChangePlayerItem = feedViewModel::changePlayerItem
            )
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

@OptIn(UnstableApi::class)
@Composable
fun FollowingPager(
    modifier: Modifier = Modifier,
    posts: LazyPagingItems<Post>,
    player: Player?,
    onInitializePlayer: () -> Unit = {},
    onReleasePlayer: () -> Unit = {},
    onChangePlayerItem: (uri: Uri?, page: Int) -> Unit = { uri: Uri?, i: Int -> }
) {
    Box(Modifier.fillMaxSize()) {
        val pagerState = rememberPagerState(pageCount = { posts.itemCount })

        val currentOnInitializePlayer by rememberUpdatedState(onInitializePlayer)
        val currentOnReleasePlayer by rememberUpdatedState(onReleasePlayer)

        LifecycleStartEffect(true) {
            currentOnInitializePlayer()
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
                        snapshotFlow { pagerState.settledPage }
                            .collect { page ->
                                val uri = posts[page]?.mediaFiles?.first()?.url?.toUri()
                                onChangePlayerItem(uri, page)
                            }
                    }

                    VerticalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 90.dp)
                    ) { page ->
                        val post = posts[page]

                        if (player != null && post != null) {
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
                                    modifier = modifier.fillMaxSize(),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

