package com.example.scrollbooker.ui.feed
import BottomBar
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.bottomBar.MainTab
import com.example.scrollbooker.navigation.containers.DefaultTabContainer
import com.example.scrollbooker.navigation.host.FeedNavHost
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.feed.components.FeedTabs
import com.example.scrollbooker.ui.sharedModules.posts.PostsPager
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FeedScreen(
    viewModel: FeedViewModel,
    bookNowPosts: LazyPagingItems<Post>,
    onOpenDrawer: () -> Unit,
    onNavigateSearch: () -> Unit,
    onNavigate: (MainTab) -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 1) { 2 }
    val selectedTabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

    var isUserSwiping by remember { mutableStateOf(false) }
    var isFirstPost by remember { mutableStateOf(false) }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPageOffsetFraction }
            .collect { offset ->
                isUserSwiping = offset != 0f
            }
    }


    Scaffold(
        bottomBar = {
            AnimatedContent(
                targetState = isFirstPost,
                transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
                label = "label"
            ) { isFirst ->
                if(isFirst) {
                    BottomBar(
                        //appointmentsNumber = mainViewModel.appointmentsState,
                        appointmentsNumber = 0,
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
                        onClick = {},
                        title = "Intervale disponibile"
                    )
                }
            }
        }
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
        ) {
            FeedTabs(
                selectedTabIndex = selectedTabIndex,
                onOpenDrawer = onOpenDrawer,
                onNavigateSearch = onNavigateSearch,
                isFirstPost = isFirstPost,
                onChangeTab = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(it)
                    }
                }
            )

            HorizontalPager(
                state = pagerState,
                beyondViewportPageCount = 1,
                pageSize = PageSize.Fill,
                key = { it },
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when(page) {
                    0 -> {
                        val posts = viewModel.followingPosts.collectAsLazyPagingItems()

                        PostsPager(
                            posts = posts,
                            isVisibleTab = pagerState.currentPage == page && !isUserSwiping,
                            onSetFirstPost = { isFirstPost = it }
                        )
                    }
                    1 -> {
                        PostsPager(
                            posts = bookNowPosts,
                            isVisibleTab = pagerState.currentPage == page && !isUserSwiping,
                            onSetFirstPost = { isFirstPost = it }
                        )
                    }
                }
            }
        }
    }
}

