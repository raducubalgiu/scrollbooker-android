package com.example.scrollbooker.ui.modules.reviews.list
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.components.core.layout.MessageScreen
import com.example.scrollbooker.entity.booking.review.domain.model.Review
import com.example.scrollbooker.entity.booking.review.domain.model.ReviewsSummary
import com.example.scrollbooker.ui.modules.reviews.summary.ReviewSummaryShimmer
import com.example.scrollbooker.ui.modules.reviews.summary.ReviewsSummarySection
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun ReviewsList(
    pagingItems: LazyPagingItems<Review>,
    summaryState: FeatureState<ReviewsSummary>,
    onRatingClick: (Int) -> Unit,
    selectedRatings: Set<Int>
) {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .background(SurfaceBG)
    ) {
        pagingItems.apply {
            when(loadState.refresh) {
                is LoadState.Loading -> {
                    item {
                        Box(Modifier.background(Background)) {
                            ReviewSummaryShimmer()
                        }
                    }
                }
                is LoadState.Error -> {
                    item { ErrorScreen() }
                }
                is LoadState.NotLoading -> {
                    if(summaryState is FeatureState.Success) {
                        val summary = summaryState.data

                        item {
                            if(summary.totalReviews > 0) {
                                ReviewsSummarySection(
                                    summary,
                                    onRatingClick,
                                    selectedRatings
                                )
                            }
                        }
                    }

                    if(pagingItems.itemCount == 0) {
                        item { MessageScreen(
                            message = stringResource(R.string.dontFoundResults),
                            icon = painterResource(R.drawable.ic_clipboard_outline)
                        ) }
                    }
                }
            }
        }

        items(pagingItems.itemCount) { index ->
            pagingItems[index]?.let { ReviewItem(it) }
        }

        pagingItems.apply {
            when (loadState.append) {
                is LoadState.Loading -> {
                    item { LoadMoreSpinner() }
                }

                is LoadState.Error -> {
                    item { Text("Ceva nu a mers cum trebuie") }
                }

                is LoadState.NotLoading -> Unit
            }
        }

        item {
            Spacer(modifier = Modifier
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