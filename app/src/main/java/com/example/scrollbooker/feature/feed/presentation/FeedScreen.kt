package com.example.scrollbooker.feature.feed.presentation
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.scrollbooker.feature.feed.presentation.components.FeedTabs
import com.example.scrollbooker.feature.feed.presentation.tabs.BookNowTab
import com.example.scrollbooker.feature.feed.presentation.tabs.FollowingTab
import kotlinx.coroutines.launch

@Composable
fun FeedScreen(
    viewModel: FeedViewModel,
    onOpenDrawer: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 0) { 2 }
    val selectedTabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()
    val isUserSwiping = remember { mutableStateOf(false) }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPageOffsetFraction }
            .collect { offset ->
                isUserSwiping.value = offset != 0f
            }
    }

    Column(Modifier.fillMaxSize().background(Color(0xFF121212))) {
        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fill,
            modifier = Modifier.fillMaxSize(),
            beyondViewportPageCount = 0,
            key = { it }
        ) { page ->
            val shouldVideoPlay = !isUserSwiping.value && pagerState.currentPage == page

            when(page) {
                0 -> BookNowTab(shouldVideoPlay)
                1 -> FollowingTab(shouldVideoPlay)
            }
        }
    }

    FeedTabs(
        selectedTabIndex = selectedTabIndex,
        onOpenDrawer = onOpenDrawer,
        onChangeTab = {
            coroutineScope.launch {
                pagerState.animateScrollToPage(it)
            }
        }
    )
}

