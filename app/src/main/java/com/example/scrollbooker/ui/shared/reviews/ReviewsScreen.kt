package com.example.scrollbooker.ui.shared.reviews
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.review.domain.model.ReviewsSummary
import com.example.scrollbooker.ui.shared.reviews.components.ReviewsSummarySection
import com.example.scrollbooker.ui.shared.reviews.tabs.ReviewsTabRow
import com.example.scrollbooker.ui.shared.reviews.tabs.VideoReviewsTab
import com.example.scrollbooker.ui.shared.reviews.tabs.WrittenReviewsTab
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun ReviewsScreen(
    viewModel: ReviewsViewModel,
    userId: Int
) {
    val scope = rememberCoroutineScope()
    val tabs = listOf(stringResource(R.string.written), stringResource(R.string.video))
    val pagerState = rememberPagerState { 2 }

    LaunchedEffect(userId) {
        viewModel.setUserId(userId)
        viewModel.setTab(ReviewsViewModel.ReviewsTab.WRITTEN)
        pagerState.scrollToPage(0)
    }

    val summaryState by viewModel.userReviewsSummary.collectAsState()
    val writtenReviews = viewModel.writeReviews.collectAsLazyPagingItems()
    val videoReviews = viewModel.videoReviews.collectAsLazyPagingItems()
    val selectedRatings by viewModel.selectedRatings.collectAsState()

    val isSummaryLoading by viewModel.summaryIsLoading.collectAsState()
    val isFirstPageLoading =
        writtenReviews.loadState.refresh is LoadState.Loading && writtenReviews.itemCount == 0
    val isInitialLoading = isSummaryLoading || isFirstPageLoading

    if(isInitialLoading) {
        LoadingScreen()
        return
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collectLatest { page ->
                viewModel.setTab(
                    if(page == 0) ReviewsViewModel.ReviewsTab.WRITTEN
                    else ReviewsViewModel.ReviewsTab.VIDEO
                )
            }
    }

    Box(Modifier.fillMaxSize()) {
        LazyColumn {
            item {
                when(summaryState) {
                    is FeatureState.Error -> ErrorScreen()
                    is FeatureState.Loading -> LoadingScreen()
                    is FeatureState.Success -> {
                        val summary = (summaryState as FeatureState.Success<ReviewsSummary>).data

                        ReviewsSummarySection(
                            summary = summary,
                            onRatingClick = { viewModel.toggleRating(it) },
                            selectedRatings = selectedRatings,
                        )
                    }
                }
            }

            stickyHeader {
                val selectedTab = pagerState.currentPage

                ReviewsTabRow(
                    tabs = tabs,
                    selectedTab = selectedTab,
                    onChangeTab = {
                        scope.launch {
                            pagerState.animateScrollToPage(it)
                            delay(200)
                            viewModel.setTab(
                                if(pagerState.settledPage == 0) ReviewsViewModel.ReviewsTab.WRITTEN
                                else ReviewsViewModel.ReviewsTab.VIDEO
                            )
                        }
                    }
                )
            }

            item {
                HorizontalPager(
                    state = pagerState,
                    beyondViewportPageCount = 0,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LocalConfiguration.current.screenHeightDp.dp * 0.9f)
                ) { page ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopStart
                    ) {
                        when(page) {
                            0 -> WrittenReviewsTab(writtenReviews)
                            1 -> VideoReviewsTab(videoReviews)
                        }
                    }
                }
            }
        }
    }
}