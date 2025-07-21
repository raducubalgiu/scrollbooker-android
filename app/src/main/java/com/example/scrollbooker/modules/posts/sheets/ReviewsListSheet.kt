package com.example.scrollbooker.modules.posts.sheets
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.components.core.tabs.Tabs
import com.example.scrollbooker.modules.reviews.summary.ReviewSummaryShimmer
import com.example.scrollbooker.modules.reviews.list.ReviewItem
import com.example.scrollbooker.modules.reviews.list.ReviewItemShimmer
import com.example.scrollbooker.modules.reviews.summary.ReviewsSummarySection
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.entity.booking.review.domain.model.ReviewsSummary
import com.example.scrollbooker.modules.reviews.ReviewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ReviewsListSheet(
    viewModel: ReviewsViewModel,
    onClose: () -> Unit,
    onRatingClick: (Int) -> Unit
) {
    val reviewsSummary by viewModel.userReviewsSummary.collectAsState()
    val reviews = viewModel.userReviews.collectAsLazyPagingItems()
    val selectedRatings = viewModel.selectedRatings.value

    val tabs = listOf("Recenzii standard", "Recenzii video")
    val pagerState = rememberPagerState(initialPage = 0 ) { 2 }

    SheetHeader(
        title = stringResource(R.string.reviews),
        onClose = onClose
    )

    LazyColumn {
        item {
            Column(Modifier.height(320.dp)) {
                when(reviewsSummary) {
                    is FeatureState.Error -> ErrorScreen()
                    is FeatureState.Loading -> ReviewSummaryShimmer()
                    is FeatureState.Success -> {
                        val summary = (reviewsSummary as FeatureState.Success<ReviewsSummary>).data

                        ReviewsSummarySection(
                            summary = summary,
                            onRatingClick = onRatingClick,
                            selectedRatings = selectedRatings,
                        )
                    }
                }
            }
        }

        stickyHeader {
            Tabs(
                tabs = tabs,
                selectedTabIndex = 0,
                onChangeTab = {}
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
                        0 -> {
                            when (reviews.loadState.refresh) {
                                is LoadState.Loading -> {
                                    Column(Modifier.fillMaxSize()) {
                                        repeat(7) { ReviewItemShimmer() }
                                    }
                                }
                                is LoadState.Error -> ErrorScreen()
                                is LoadState.NotLoading -> {
                                    LazyColumn {
                                        items(reviews.itemCount) { index ->
                                            reviews[index]?.let { review ->
                                                ReviewItem(review = review)
                                            }
                                        }

                                        item {
                                            when(reviews.loadState.append) {
                                                is LoadState.Loading -> LoadMoreSpinner()
                                                is LoadState.Error -> "Something went wrong"
                                                is LoadState.NotLoading -> Unit
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        1 -> {
                            LazyColumn {
                                items(40) {
                                    Box(Modifier.fillMaxSize()) {
                                        Text("Index ${it}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}