package com.example.scrollbooker.shared.review.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.shared.review.domain.model.Review
import com.example.scrollbooker.shared.review.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow

class GetReviewsUseCase(
    private val repository: ReviewRepository
) {
    operator fun invoke(userId: Int, ratings: Set<Int>?): Flow<PagingData<Review>> {
        return repository.getReviews(userId, ratings)
    }
}