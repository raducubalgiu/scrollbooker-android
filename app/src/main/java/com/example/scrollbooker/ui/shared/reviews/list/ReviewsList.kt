package com.example.scrollbooker.ui.shared.reviews.list
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.ui.shared.reviews.summary.ReviewsSummarySection
import com.example.scrollbooker.ui.social.UserSocialViewModel
import com.example.scrollbooker.ui.theme.SurfaceBG

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsList(
    viewModel: UserSocialViewModel,
    onRatingClick: (Int) -> Unit,
) {
    val reviews = viewModel.userReviews.collectAsLazyPagingItems()
    val summaryState by viewModel.userReviewsSummary.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val selectedRatings by viewModel.selectedRatings

    val refreshState = reviews.loadState.refresh
    val appendState = reviews.loadState.append

    when(refreshState) {
        is LoadState.Loading -> { LoadingScreen() }
        is LoadState.Error -> ErrorScreen()
        is LoadState.NotLoading -> {
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { viewModel.refreshReviews() }
            ) {
                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .background(SurfaceBG)
                ) {
                    item {
                        when (val summary = summaryState) {
                            is FeatureState.Success -> {
                                if (summary.data.totalReviews > 0) {
                                    ReviewsSummarySection(
                                        summary = summary.data,
                                        onRatingClick,
                                        selectedRatings
                                    )
                                }
                            }
                            else -> Unit
                        }
                    }

                    items(reviews.itemCount) { index ->
                        reviews[index]?.let { ReviewItem(it) }
                    }

                    item {
                        when(appendState) {
                            is LoadState.Error -> Text("Ceva nu a mers cum trebuie")
                            is LoadState.Loading -> LoadMoreSpinner()
                            is LoadState.NotLoading -> Unit
                        }
                    }

                    item {
                        Spacer(
                            modifier = Modifier
                                .fillMaxSize()
                                .height(
                                    WindowInsets.safeContent
                                        .only(WindowInsetsSides.Bottom)
                                        .asPaddingValues()
                                        .calculateBottomPadding()
                                )
                        )
                    }
                }
            }
        }
    }
}