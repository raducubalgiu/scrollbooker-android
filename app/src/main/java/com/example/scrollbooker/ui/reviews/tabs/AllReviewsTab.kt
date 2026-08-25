package com.example.scrollbooker.ui.reviews.tabs
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.layout.MessageScreen
import com.example.scrollbooker.components.customized.LoadMoreSpinner
import com.example.scrollbooker.core.util.rememberFlingBehavior
import com.example.scrollbooker.entity.booking.review.domain.model.Review
import com.example.scrollbooker.ui.reviews.ReviewsViewModel
import com.example.scrollbooker.ui.reviews.components.ReviewCard

@Composable
fun AllReviewsTab(
    viewModel: ReviewsViewModel,
    allReviews: LazyPagingItems<Review>
) {

    val flingBehavior = rememberFlingBehavior()

    when (allReviews.loadState.refresh) {
        is LoadState.Loading -> {
            LoadingScreen(
                modifier = Modifier.padding(top = 50.dp),
                arrangement = Arrangement.Top
            )
        }
        is LoadState.Error -> {
            ErrorScreen(
                modifier = Modifier.padding(top = 50.dp),
                arrangement = Arrangement.Top
            )
        }
        is LoadState.NotLoading -> {
            if(allReviews.itemCount == 0) {
                MessageScreen(
                    modifier = Modifier.padding(top = 50.dp),
                    arrangement = Arrangement.Top,
                    icon = painterResource(R.drawable.ic_clipboard_check_outline),
                    message = stringResource(R.string.notFoundWrittenReviews),
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                flingBehavior = flingBehavior
            ) {
                items(allReviews.itemCount) { index ->
                    allReviews[index]?.let { review ->
                        val reviewUi by viewModel.observeReviewUi(review.id)
                            .collectAsStateWithLifecycle()

                        ReviewCard(
                            review = review,
                            reviewUi = reviewUi,
                            onLike = {
                                viewModel.toggleLike(review.id, review.productBusinessOwner.id)
                            }
                        )
                    }
                }

                item {
                    when(allReviews.loadState.append) {
                        is LoadState.Loading -> LoadMoreSpinner()
                        else -> Unit
                    }
                }
            }
        }
    }
}