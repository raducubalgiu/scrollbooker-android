package com.example.scrollbooker.entity.booking.review.domain.useCase
import com.example.scrollbooker.entity.booking.review.domain.repository.ReviewRepository
import javax.inject.Inject

class DeleteWrittenReviewUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    suspend operator fun invoke(reviewId: Int): Result<Unit> = runCatching {
        repository.deleteWrittenReview(reviewId)
    }
}