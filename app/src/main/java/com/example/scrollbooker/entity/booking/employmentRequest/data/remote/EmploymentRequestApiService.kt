package com.example.scrollbooker.entity.booking.employmentRequest.data.remote
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @PUT("employment-requests/{employmentId}")
    suspend fun respondEmploymentRequest(
        @Body requestRespondDto: EmploymentRequestRespondDto,
        @Path("employmentId") employmentId: Int
    )
}