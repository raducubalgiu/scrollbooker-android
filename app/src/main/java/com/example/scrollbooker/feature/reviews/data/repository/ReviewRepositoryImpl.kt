package com.example.scrollbooker.feature.reviews.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.scrollbooker.feature.reviews.data.remote.ReviewPagingSource
import com.example.scrollbooker.feature.reviews.data.remote.ReviewsApiService
import com.example.scrollbooker.feature.reviews.domain.model.Review
import com.example.scrollbooker.feature.reviews.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val apiService: ReviewsApiService
): ReviewRepository {
    override fun getReviews(userId: Int): Flow<PagingData<Review>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { ReviewPagingSource(apiService, userId) }
        ).flow
    }
}