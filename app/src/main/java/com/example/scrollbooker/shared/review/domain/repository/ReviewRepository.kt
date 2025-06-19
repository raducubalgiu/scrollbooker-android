package com.example.scrollbooker.shared.review.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.shared.review.domain.model.Review
import com.example.scrollbooker.shared.review.domain.model.ReviewsSummary
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {
    fun getReviews(userId: Int, ratings: Set<Int>?): Flow<PagingData<Review>>
    suspend fun getReviewsSummary(userId: Int): ReviewsSummary
}