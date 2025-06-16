package com.example.scrollbooker.feature.reviews.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.ErrorScreen
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.core.util.LoadingScreen
import com.example.scrollbooker.core.util.MessageScreen
import com.example.scrollbooker.feature.reviews.domain.model.Review
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun ReviewsList(
    pagingItems: LazyPagingItems<Review>
) {
    val breakdown = listOf(
        RatingBreakdown(5, 228),
        RatingBreakdown(4, 21),
        RatingBreakdown(3, 2),
        RatingBreakdown(2, 2),
        RatingBreakdown(1, 4),
    )

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

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .background(SurfaceBG)
    ) {
        item {
            ReviewsSummary(
                averageRating = 4.8f,
                totalReviews = 100,
                breakdown = breakdown
            )
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
    }
}