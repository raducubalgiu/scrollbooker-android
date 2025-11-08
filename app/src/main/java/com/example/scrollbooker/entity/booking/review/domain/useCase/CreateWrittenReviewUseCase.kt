package com.example.scrollbooker.entity.booking.review.domain.useCase
import com.example.scrollbooker.entity.booking.review.data.remote.ReviewCreateRequest
import com.example.scrollbooker.entity.booking.review.domain.repository.ReviewRepository
import javax.inject.Inject

class CreateWrittenReviewUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    suspend operator fun invoke(appointmentId: Int, request: ReviewCreateRequest): Result<Unit> = runCatching {
        repository.createWrittenReview(appointmentId, request)
    }
}