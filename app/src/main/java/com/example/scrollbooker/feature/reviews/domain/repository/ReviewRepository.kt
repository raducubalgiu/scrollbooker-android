package com.example.scrollbooker.feature.reviews.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.feature.reviews.domain.model.Review
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {
    fun getReviews(userId: Int): Flow<PagingData<Review>>
}