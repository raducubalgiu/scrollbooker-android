package com.example.scrollbooker.ui.shared.reviews.tabs

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
import com.example.scrollbooker.entity.booking.review.domain.model.Review
import com.example.scrollbooker.ui.shared.reviews.ReviewsViewModel
import com.example.scrollbooker.ui.shared.reviews.components.ReviewCard
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber

@Composable
fun WrittenReviewsTab(
    viewModel: ReviewsViewModel,
    writtenReviews: LazyPagingItems<Review>,
    onNavigateToReviewDetail: () -> Unit
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
                        val reviewUi by viewModel.observeReviewUi(review.id)
                            .collectAsStateWithLifecycle()

                        ReviewCard(
                            reviewUi = reviewUi,
                            onNavigateToReviewDetail = onNavigateToReviewDetail,
                            customerAvatar = review.customer.avatar,
                            productBusinessOwnerAvatar = review.productBusinessOwner.avatar,
                            customerFullName = review.customer.fullName,
                            rating = review.rating,
                            review = review.review,
                            createdAt = "${
                                OffsetDateTime.parse(review.createdAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                                    .format(DateTimeFormatter.ofPattern("dd MM yyyy, HH:mm"))
                            }",
                            onLike = {
                                viewModel.toggleLike(review.id, review.productBusinessOwner.id)
                            }
                        )
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