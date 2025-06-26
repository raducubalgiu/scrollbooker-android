package com.example.scrollbooker.entity.review.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.entity.review.domain.model.Review
import com.example.scrollbooker.entity.review.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetReviewsUseCase(
    private val repository: ReviewRepository
) {
    operator fun invoke(userId: Int?, ratings: Set<Int>?): Flow<PagingData<Review>> {
        return if (userId != null) {
            repository.getReviews(userId, ratings)
        } else {
            flowOf(PagingData.empty())
        }
    }
}