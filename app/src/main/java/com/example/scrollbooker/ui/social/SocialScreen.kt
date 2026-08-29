package com.example.scrollbooker.ui.social
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.tabs.Tabs
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.layout.MessageScreen
import com.example.scrollbooker.navigation.navigators.SocialParam
import com.example.scrollbooker.navigation.navigators.UserProfileParam
import com.example.scrollbooker.ui.reviews.ReviewsSection
import com.example.scrollbooker.ui.reviews.ReviewsViewModel
import com.example.scrollbooker.ui.social.tab.UserFollowersTab
import com.example.scrollbooker.ui.social.tab.UserFollowingsTab
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SocialScreen(
    viewModal: SocialViewModel,
    reviewsViewModel: ReviewsViewModel,
    socialParam: SocialParam,
    onBack: () -> Unit,
    onNavigateUserProfile: (param: UserProfileParam) -> Unit,
    onNavigateToVideoReviewDetail: (index: Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    val tabIndex by viewModal.selectedTabIndex.collectAsStateWithLifecycle()

    val tabs = remember {
        SocialTab.getTabs()
    }

    val pagerState = rememberPagerState(initialPage = tabIndex) { tabs.size }
    val selectedTabIndex = pagerState.currentPage

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collectLatest { viewModal.setSelectedTabIndex(it) }
    }

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
                tabs = tabs,
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
                        SocialTab.Reviews -> {
                            if(socialParam.businessId != null) {
                                ReviewsSection(
                                    viewModel = reviewsViewModel,
                                    onNavigateToVideoReviewDetail = onNavigateToVideoReviewDetail
                                )
                            } else {
                                MessageScreen(
                                    icon = painterResource(R.drawable.ic_clipboard_check_outline),
                                    message = stringResource(R.string.notFoundReviews),
                                )
                            }
                        }
                        SocialTab.Followers -> UserFollowersTab(viewModal, onNavigateUserProfile)
                        SocialTab.Followings -> UserFollowingsTab(viewModal, onNavigateUserProfile)
                    }
                }
            }
        }
    }
}
