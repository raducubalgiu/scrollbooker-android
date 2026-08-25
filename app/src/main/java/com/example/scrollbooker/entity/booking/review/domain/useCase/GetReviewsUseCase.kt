package com.example.scrollbooker.entity.booking.review.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.entity.booking.review.domain.model.Review
import com.example.scrollbooker.entity.booking.review.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetReviewsUseCase(
    private val repository: ReviewRepository
) {
    operator fun invoke(businessId: Int?, employeeId: Int?, ratings: Set<Int>?): Flow<PagingData<Review>> {
        return if (businessId != null) {
            repository.getReviews(businessId, employeeId, ratings)
        } else {
            flowOf(PagingData.empty())
        }
    }
}