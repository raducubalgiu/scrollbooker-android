package com.example.scrollbooker.entity.review.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.scrollbooker.entity.review.data.mappers.toDomain
import com.example.scrollbooker.entity.review.data.remote.ReviewPagingSource
import com.example.scrollbooker.entity.review.data.remote.ReviewsApiService
import com.example.scrollbooker.entity.review.domain.model.Review
import com.example.scrollbooker.entity.review.domain.model.ReviewsSummary
import com.example.scrollbooker.entity.review.domain.repository.ReviewRepository
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
}