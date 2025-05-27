package com.example.scrollbooker.feature.feed.presentation
import android.util.Log
import androidx.compose.foundation.layout.Box
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
import com.example.scrollbooker.feature.feed.presentation.components.FeedTabs
import com.example.scrollbooker.feature.feed.presentation.tabs.BookNowTab
import com.example.scrollbooker.feature.feed.presentation.tabs.FollowingTab
import kotlinx.coroutines.launch

@Composable
fun FeedScreen(onOpenDrawer: () -> Unit) {
    val pagerState = rememberPagerState(initialPage = 0) { 2 }
    val selectedTabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()
    val isUserSwiping = remember { mutableStateOf(false) }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPageOffsetFraction }
            .collect { offset ->
                Log.d("Offset!!", offset.toString())
                isUserSwiping.value = offset != 0f
            }
    }

    Box(Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fill,
            beyondViewportPageCount = 1,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val shouldVideoPlay = !isUserSwiping.value && pagerState.currentPage == page

            when(page) {
                0 -> BookNowTab(shouldVideoPlay)
                1 -> FollowingTab(shouldVideoPlay)
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

