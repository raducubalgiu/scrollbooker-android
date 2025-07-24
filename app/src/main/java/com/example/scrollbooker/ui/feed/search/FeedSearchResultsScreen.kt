package com.example.scrollbooker.ui.feed.search
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.ui.theme.Background
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scrollbooker.ui.feed.components.FeedSearchResultsTabRow
import com.example.scrollbooker.ui.feed.search.tab.FeedSearchTab
import com.example.scrollbooker.ui.feed.search.tab.users.FeedSearchUsersTab
import com.example.scrollbooker.ui.feed.search.tab.users.FeedSearchUsersViewModel

@Composable
fun FeedSearchResultsScreen(
    viewModel: FeedSearchViewModel,
    onBack: () -> Unit
) {
    val feedSearchUsersViewModel: FeedSearchUsersViewModel = hiltViewModel()

    val tabs = remember { FeedSearchTab.getTabs }

    val pagerState = rememberPagerState(initialPage = 0 ) { 6 }
    val selectedTabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

    val query by viewModel.currentSearch.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
    )  {
        FeedSearchHeader(
            value = query,
            onValueChange = {},
            readOnly = true,
            onSearch = {},
            onClearInput = onBack,
            onClick = onBack,
            onBack = onBack,
        )

        Spacer(Modifier.height(SpacingXS))

        FeedSearchResultsTabRow(
            selectedTabIndex = selectedTabIndex,
            tabs = tabs.map { it.route },
            onChangeTab = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(it)
                }
            }
        )

        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 0,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when(page) {
                0 -> Box(Modifier.fillMaxSize()) {
                    Text("For You")
                }
                1 -> {
                    FeedSearchUsersTab(
                        query = query,
                        viewModel = feedSearchUsersViewModel
                    )
                }
                2 -> Box(Modifier.fillMaxSize()) {
                    Text("Servicii")
                }
                3 -> Box(Modifier.fillMaxSize()) {
                    Text("Last Minute")
                }
                4 -> Box(Modifier.fillMaxSize()) {
                    Text("Instant Booking")
                }
                5 -> Box(Modifier.fillMaxSize()) {
                    Text("Reviews")
                }
            }
        }
    }
}