package com.example.scrollbooker.entity.employmentRequest.data.remote
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EmploymentRequestsApiService {
    @GET("users/{userId}/employment-requests")
    suspend fun getUserEmploymentRequests(
        @Path("userId") userId: Int,
    ): List<EmploymentRequestDto>

    @POST("employment-requests")
    suspend fun createEmploymentRequest(
        @Body request: EmploymentRequestCreateDto
    )
}