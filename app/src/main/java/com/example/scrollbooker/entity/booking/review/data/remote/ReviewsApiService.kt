package com.example.scrollbooker.entity.booking.review.data.remote

import com.example.scrollbooker.core.util.PaginatedResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewsApiService {
    @GET("businesses/{businessId}/reviews")
    suspend fun getReviews(
        @Path("businessId") businessId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("employee_id") employeeId: Int?,
        @Query("ratings") ratings: List<Int>?
    ): PaginatedResponseDto<ReviewDto>

    @GET("businesses/{businessId}/reviews-summary")
    suspend fun getReviewsSummary(
        @Path("businessId") businessId: Int,
        @Query("employee_id") employeeId: Int?
    ): ReviewsSummaryDto

    @POST("appointments/{appointmentId}/create-review")
    suspend fun createWrittenReview(
        @Path("appointmentId") appointmentId: Int,
        @Body request: ReviewCreateRequest
    ): ReviewMiniDto

    @PUT("reviews/{reviewId}")
    suspend fun updateWrittenReview(
        @Path("reviewId") reviewId: Int,
        @Body request: ReviewUpdateRequest
    ): ReviewMiniDto

    @DELETE("reviews/{reviewId}")
    suspend fun deleteWrittenReview(
        @Path("reviewId") reviewId: Int
    )

    @POST("reviews/{reviewId}/likes")
    suspend fun likeWrittenReview(
        @Path("reviewId") reviewId: Int
    )

    @DELETE("reviews/{reviewId}/likes")
    suspend fun unlikeWrittenReview(
        @Path("reviewId") reviewId: Int
    )
}