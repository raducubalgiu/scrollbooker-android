package com.example.scrollbooker.ui.shared.reviews
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.rememberCollapsingNestedScroll
import com.example.scrollbooker.entity.booking.review.domain.model.ReviewsSummary
import com.example.scrollbooker.ui.shared.reviews.components.ReviewsSummarySection
import com.example.scrollbooker.ui.shared.reviews.tabs.ReviewsTabRow
import com.example.scrollbooker.ui.shared.reviews.tabs.VideoReviewsTab
import com.example.scrollbooker.ui.shared.reviews.tabs.WrittenReviewsTab
import com.example.scrollbooker.ui.theme.Background
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsScreen(
    viewModel: ReviewsViewModel,
    userId: Int,
    onNavigateToReviewDetail: () -> Unit,
    onClose: (() -> Unit)? = null,
) {
    val scope = rememberCoroutineScope()
    val tabs = listOf(stringResource(R.string.written), stringResource(R.string.video))
    val pagerState = rememberPagerState { 2 }

    LaunchedEffect(userId) {
        viewModel.setUserId(userId)
        viewModel.clearRatings()
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

    var headerHeightPx by remember { mutableIntStateOf(0) }
    var headerOffset by remember { mutableFloatStateOf(0f) }

    val nestedScrollConnection = rememberCollapsingNestedScroll(
        headerHeightPx = headerHeightPx,
        headerOffset = headerOffset,
        onHeaderOffsetChanged = { headerOffset = it }
    )

    Column(Modifier.fillMaxSize()) {
        SheetHeader(
            modifier = Modifier
                .background(Background)
                .zIndex(25f),
            title = stringResource(R.string.reviews),
            onClose = {
                onClose?.invoke()
            }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection)
        ) {
            Column(
                modifier = Modifier
                    .graphicsLayer {
                        translationY = headerHeightPx + headerOffset
                    }
            ) {
                ReviewsTabRow(
                    tabs = tabs,
                    selectedTab = pagerState.currentPage,
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

                HorizontalPager(
                    state = pagerState,
                    beyondViewportPageCount = 0
                ) { page ->
                    when(page) {
                        0 -> {
                            WrittenReviewsTab(
                                viewModel = viewModel,
                                writtenReviews = writtenReviews,
                                onNavigateToReviewDetail = onNavigateToReviewDetail
                            )
                        }
                        1 -> VideoReviewsTab(videoReviews)
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset { IntOffset(0, headerOffset.roundToInt()) }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onSizeChanged { size -> headerHeightPx = size.height }
                ) {
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
            }
        }
    }
}
