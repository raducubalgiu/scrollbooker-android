package com.example.scrollbooker.entity.booking.review.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.entity.booking.review.data.remote.ReviewCreateRequest
import com.example.scrollbooker.entity.booking.review.domain.model.Review
import com.example.scrollbooker.entity.booking.review.domain.model.ReviewMini
import com.example.scrollbooker.entity.booking.review.domain.model.ReviewsSummary
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {
    fun getReviews(userId: Int, ratings: Set<Int>?): Flow<PagingData<Review>>
    suspend fun getReviewsSummary(userId: Int): ReviewsSummary
    suspend fun createWrittenReview(appointmentId: Int, request: ReviewCreateRequest): ReviewMini
    suspend fun updateWrittenRequest(
        reviewId: Int,
        review: String?,
        rating: Int,
    ): ReviewMini
    suspend fun deleteWrittenReview(reviewId: Int)
    suspend fun likeWrittenReview(reviewId: Int)
    suspend fun unlikeWrittenReview(reviewId: Int)
}