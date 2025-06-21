package com.example.scrollbooker.screens.feed
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding

@Composable
fun FeedScreen(
    viewModel: FeedViewModel,
    onOpenDrawer: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 0) { 2 }
    val selectedTabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()
    val isUserSwiping = remember { mutableStateOf(false) }

    Box( modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF121212))
    ) {
        Row(modifier = Modifier.fillMaxWidth().statusBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.clickable(onClick = onOpenDrawer)) {
                Box(modifier = Modifier.padding(BasePadding),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Outlined.Menu,
                        contentDescription = null,
                        tint = Color(0xFFE0E0E0)
                    )
                }
            }

            Box(modifier = Modifier.clickable(onClick = {})) {
               Box(modifier = Modifier.padding(BasePadding),
                   contentAlignment = Alignment.Center
               ) {
                   Icon(
                       modifier = Modifier.size(30.dp),
                       imageVector = Icons.Outlined.Search,
                       contentDescription = null,
                       tint = Color(0xFFE0E0E0),
                   )
               }
            }
        }
    }

//    LaunchedEffect(pagerState) {
//        snapshotFlow { pagerState.currentPageOffsetFraction }
//            .collect { offset ->
//                isUserSwiping.value = offset != 0f
//            }
//    }
//
//    Column(Modifier.fillMaxSize().background(Color(0xFF121212))) {
//        HorizontalPager(
//            state = pagerState,
//            pageSize = PageSize.Fill,
//            modifier = Modifier.fillMaxSize(),
//            beyondViewportPageCount = 0,
//            key = { it }
//        ) { page ->
//            val shouldVideoPlay = !isUserSwiping.value && pagerState.currentPage == page
//
//            when(page) {
//                0 -> BookNowTab(shouldVideoPlay)
//                1 -> FollowingTab(shouldVideoPlay)
//            }
//        }
//    }
//
//    FeedTabs(
//        selectedTabIndex = selectedTabIndex,
//        onOpenDrawer = onOpenDrawer,
//        onChangeTab = {
//            coroutineScope.launch {
//                pagerState.animateScrollToPage(it)
//            }
//        }
//    )
}

