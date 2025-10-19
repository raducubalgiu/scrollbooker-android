package com.example.scrollbooker.ui.shared.reviews.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.layout.MessageScreen
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.entity.booking.review.domain.model.Review
import com.example.scrollbooker.ui.shared.reviews.components.ReviewItem

@Composable
fun WrittenReviewsTab(
    writtenReviews: LazyPagingItems<Review>
) {
    when (writtenReviews.loadState.refresh) {
        is LoadState.Loading -> {
            LoadingScreen(
                modifier = Modifier.padding(top = 50.dp),
                arrangement = Arrangement.Top
            )
        }
        is LoadState.Error -> ErrorScreen()
        is LoadState.NotLoading -> {
            if(writtenReviews.itemCount == 0) {
                MessageScreen(
                    modifier = Modifier.padding(top = 50.dp),
                    arrangement = Arrangement.Top,
                    icon = painterResource(R.drawable.ic_clipboard_check_outline),
                    message = stringResource(R.string.notFoundWrittenReviews),
                )
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(writtenReviews.itemCount) { index ->
                    writtenReviews[index]?.let { review ->
                        ReviewItem(review = review)
                    }
                }

                item {
                    when(writtenReviews.loadState.append) {
                        is LoadState.Loading -> LoadMoreSpinner()
                        else -> Unit
                    }
                }
            }
        }
    }
}