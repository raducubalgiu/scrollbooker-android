package com.example.scrollbooker.entity.booking.review.domain.useCase
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.review.domain.model.ReviewsSummary
import com.example.scrollbooker.entity.booking.review.domain.repository.ReviewRepository
import timber.log.Timber
import javax.inject.Inject

class GetReviewsSummaryUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    suspend operator fun invoke(userId: Int): FeatureState<ReviewsSummary> {
        return try {
            val response = repository.getReviewsSummary(userId)
            FeatureState.Success(response)

        } catch (e: Exception) {
            Timber.tag("Reviews Summary").e("ERROR: on Fetching Reviews Summary $e")
            FeatureState.Error(e)
        }
    }
}