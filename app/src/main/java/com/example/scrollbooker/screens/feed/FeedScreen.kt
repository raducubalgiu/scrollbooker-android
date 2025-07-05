package com.example.scrollbooker.screens.feed
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.screens.feed.components.FeedTabs
import kotlinx.coroutines.launch
import com.example.scrollbooker.modules.posts.common.PostsList

@Composable
fun FeedScreen(
    viewModel: FeedViewModel,
    onOpenDrawer: () -> Unit,
    onNavigateSearch: () -> Unit
) {
    val tabCount = 2
    val pagerState = rememberPagerState(initialPage = 0) { tabCount }
    val selectedTabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

    var isUserSwiping by remember { mutableStateOf(false) }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPageOffsetFraction }
            .collect { offset ->
                isUserSwiping = offset != 0f
            }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF121212))
    ) {
        FeedTabs(
            selectedTabIndex = selectedTabIndex,
            onOpenDrawer = onOpenDrawer,
            onNavigateSearch = onNavigateSearch,
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
                    val posts = viewModel.bookNowPosts.collectAsLazyPagingItems()
                    PostsList(
                        posts = posts,
                        isVisibleTab = pagerState.currentPage == page && !isUserSwiping
                    )
                }
                1 -> {
                    val posts = viewModel.followingPosts.collectAsLazyPagingItems()
                    PostsList(
                        posts = posts,
                        isVisibleTab = pagerState.currentPage == page && !isUserSwiping
                    )
                }
            }
        }
    }
}

