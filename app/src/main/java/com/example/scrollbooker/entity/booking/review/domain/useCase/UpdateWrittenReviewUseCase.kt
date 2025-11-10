package com.example.scrollbooker.entity.booking.review.domain.useCase
import com.example.scrollbooker.entity.booking.review.domain.model.ReviewMini
import com.example.scrollbooker.entity.booking.review.domain.repository.ReviewRepository
import javax.inject.Inject

class UpdateWrittenReviewUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    suspend operator fun invoke(
        reviewId: Int,
        review: String?,
        rating: Int
    ): Result<ReviewMini> = runCatching {
        repository.updateWrittenRequest(reviewId, review, rating)
    }
}