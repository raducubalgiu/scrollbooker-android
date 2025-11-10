package com.example.scrollbooker.entity.booking.review.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.scrollbooker.entity.booking.review.data.mappers.toDomain
import com.example.scrollbooker.entity.booking.review.data.remote.ReviewCreateRequest
import com.example.scrollbooker.entity.booking.review.data.remote.ReviewPagingSource
import com.example.scrollbooker.entity.booking.review.data.remote.ReviewUpdateRequest
import com.example.scrollbooker.entity.booking.review.data.remote.ReviewsApiService
import com.example.scrollbooker.entity.booking.review.domain.model.Review
import com.example.scrollbooker.entity.booking.review.domain.model.ReviewMini
import com.example.scrollbooker.entity.booking.review.domain.model.ReviewsSummary
import com.example.scrollbooker.entity.booking.review.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val apiService: ReviewsApiService
): ReviewRepository {
    override fun getReviews(userId: Int, ratings: Set<Int>?): Flow<PagingData<Review>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { ReviewPagingSource(apiService, userId, ratings) }
        ).flow
    }

    override suspend fun getReviewsSummary(userId: Int): ReviewsSummary {
        return apiService.getReviewsSummary(userId).toDomain()
    }

    override suspend fun createWrittenReview(
        appointmentId: Int,
        request: ReviewCreateRequest
    ): ReviewMini {
        return apiService.createWrittenReview(appointmentId, request).toDomain()
    }

    override suspend fun updateWrittenRequest(
        reviewId: Int,
        review: String?,
        rating: Int
    ): ReviewMini {
        val request = ReviewUpdateRequest(
            review = review,
            rating = rating
        )
        return apiService.updateWrittenReview(reviewId, request).toDomain()
    }

    override suspend fun deleteWrittenReview(reviewId: Int) {
        return apiService.deleteWrittenReview(reviewId)
    }
}