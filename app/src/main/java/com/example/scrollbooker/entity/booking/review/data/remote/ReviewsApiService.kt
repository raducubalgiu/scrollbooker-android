package com.example.scrollbooker.entity.booking.review.data.remote

import com.example.scrollbooker.core.util.PaginatedResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
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

    @POST("appointments/{appointmentId}/create-review")
    suspend fun createWrittenReview(
        @Path("appointmentId") appointmentId: Int,
        @Body request: ReviewCreateRequest
    ): ReviewMiniDto

    @PATCH("reviews/{reviewId}")
    suspend fun updateWrittenReview(
        @Path("reviewId") reviewId: Int,
        @Body request: ReviewUpdateRequest
    ): ReviewMiniDto

    @DELETE("reviews/{reviewId}")
    suspend fun deleteWrittenReview(
        @Path("reviewId") reviewId: Int
    )
}