package com.example.scrollbooker.feature.reviews.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.ErrorScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.core.util.LoadingScreen
import com.example.scrollbooker.core.util.MessageScreen
import com.example.scrollbooker.feature.reviews.domain.model.Review
import com.example.scrollbooker.feature.reviews.domain.model.ReviewsSummary
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
        item {
            when(summaryState) {
                is FeatureState.Success -> {
                    val summary = summaryState.data
                    ReviewsSummarySection(
                        summary,
                        onRatingClick,
                        selectedRatings
                    )
                }
                is FeatureState.Loading -> Unit
                is FeatureState.Error -> Unit
            }
        }

        item {
            pagingItems.apply {
                when(loadState.refresh) {
                    is LoadState.Loading -> LoadingScreen()
                    is LoadState.Error -> ErrorScreen()
                    is LoadState.NotLoading -> {
                        if(pagingItems.itemCount == 0) {
                            MessageScreen(
                                message = stringResource(R.string.dontFoundResults),
                                icon = Icons.Outlined.Book
                            )
                        }
                    }
                }
            }
        }

        items(pagingItems.itemCount) { index ->
            pagingItems.apply {
                if(loadState.refresh != LoadState.Loading) {
                    pagingItems[index]?.let { ReviewItem(it) }
                }
            }
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
    }
}