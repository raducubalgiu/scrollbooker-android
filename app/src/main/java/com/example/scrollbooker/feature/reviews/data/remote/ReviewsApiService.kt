package com.example.scrollbooker.feature.reviews.data.remote

import com.example.scrollbooker.core.util.PaginatedResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewsApiService {
    @GET("users/{userId}/reviews")
    suspend fun getReviews(
        @Path("userId") userId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("ratings") ratings: List<Int>?
    ): PaginatedResponseDto<ReviewDto>

    @GET("users/{userId}/reviews-summary")
    suspend fun getReviewsSummary(
        @Path("userId") userId: Int
    ): ReviewsSummaryDto
}