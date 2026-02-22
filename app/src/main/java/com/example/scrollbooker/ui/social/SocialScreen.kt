package com.example.scrollbooker.ui.social
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.tabs.Tabs
import kotlinx.coroutines.launch
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.navigation.navigators.NavigateSocialParam
import com.example.scrollbooker.ui.shared.reviews.ReviewsScreen
import com.example.scrollbooker.ui.shared.reviews.ReviewsViewModel
import com.example.scrollbooker.ui.social.tab.UserFollowersTab
import com.example.scrollbooker.ui.social.tab.UserFollowingsTab

@Composable
fun SocialScreen(
    viewModal: SocialViewModel,
    socialParam: NavigateSocialParam,
    onBack: () -> Unit,
    onNavigateUserProfile: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()

    val tabs = remember {
        SocialTab.getTabs()
    }

    val reviewsViewModel: ReviewsViewModel = hiltViewModel()

    val pagerState = rememberPagerState(initialPage = socialParam.tabIndex ) { tabs.size }
    val selectedTabIndex = pagerState.currentPage

    Scaffold(
        topBar = {
            Header(
                title = socialParam.username,
                onBack = onBack
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(top = innerPadding.calculateTopPadding())
        ) {
            Tabs(
                tabs = tabs.map { it.route },
                selectedTabIndex = selectedTabIndex,
                indicatorPadding = 35.dp,
                onChangeTab = { scope.launch { pagerState.animateScrollToPage(it) } }
            )

            HorizontalPager(
                state = pagerState,
                beyondViewportPageCount = 0,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val post = tabs[page]

                key(post) {
                    when(post) {
                        SocialTab.Reviews -> ReviewsScreen(
                            userId = socialParam.userId,
                            viewModel = reviewsViewModel,
                            onNavigateToReviewDetail = {}
                        )
                        SocialTab.Followers -> UserFollowersTab(viewModal, onNavigateUserProfile)
                        SocialTab.Followings -> UserFollowingsTab(viewModal, onNavigateUserProfile)
                    }
                }
            }
        }
    }
}