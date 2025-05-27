package com.example.scrollbooker.feature.feed.presentation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.scrollbooker.feature.feed.presentation.components.FeedTabs
import com.example.scrollbooker.feature.feed.presentation.tabs.BookNowTab
import com.example.scrollbooker.feature.feed.presentation.tabs.FollowingTab
import kotlinx.coroutines.launch

@Composable
fun FeedScreen(onOpenDrawer: () -> Unit) {
    val pagerState = rememberPagerState(initialPage = 0) { 2 }
    val selectedTabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

    Box(Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState) { page ->
            when(page) {
                0 -> BookNowTab()
                1 -> FollowingTab()
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
}

