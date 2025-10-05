package com.example.scrollbooker.ui.social
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.components.core.tabs.Tabs
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.ui.social.tab.BookingsTab
import com.example.scrollbooker.ui.social.tab.UserFollowersTab
import com.example.scrollbooker.ui.social.tab.UserFollowingsTab
import com.example.scrollbooker.ui.modules.reviews.list.ReviewsList

@Composable
fun UserSocialScreen(
    viewModal: UserSocialViewModel,
    initialPage: Int,
    username: String,
    isBusinessOrEmployee: Boolean,
    onBack: () -> Unit,
    onNavigateUserProfile: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    var didLoadSummary by rememberSaveable { mutableStateOf(false) }

    val tabs = remember(isBusinessOrEmployee) {
        SocialTab.getTabs(isBusinessOrEmployee)
    }

    val pagerState = rememberPagerState(initialPage = initialPage ) { tabs.size }
    val selectedTabIndex = pagerState.currentPage

    LaunchedEffect(pagerState.currentPage) {
        if(pagerState.currentPage == 0 && !didLoadSummary && isBusinessOrEmployee) {
            viewModal.loadUserReviews()
            didLoadSummary = true
        }
    }

    Scaffold(
        topBar = {
            Header(title = username, onBack = onBack)
        }
    ) { innerPadding ->
        Column(Modifier.padding(top = innerPadding.calculateTopPadding())) {
            Tabs(
                tabs = tabs.map { it.route },
                selectedTabIndex,
                indicatorPadding = 35.dp,
                onChangeTab = { scope.launch { pagerState.animateScrollToPage(it) } }
            )

            HorizontalPager(
                state = pagerState,
                beyondViewportPageCount = 0,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when(tabs[page]) {
                    SocialTab.Bookings -> BookingsTab()
                    SocialTab.Reviews -> {
                        val pagingItems = viewModal.userReviews.collectAsLazyPagingItems()
                        val summaryState by viewModal.userReviewsSummary.collectAsState()
                        val selectedRatings by viewModal.selectedRatings

                        ReviewsList(
                            pagingItems,
                            summaryState,
                            onRatingClick = { viewModal.toggleRatingFilter(it) },
                            selectedRatings = selectedRatings
                        )
                    }
                    SocialTab.Followers -> UserFollowersTab(viewModal, onNavigateUserProfile)
                    SocialTab.Followings -> UserFollowingsTab(viewModal, onNavigateUserProfile)
                }
            }
        }
    }
}