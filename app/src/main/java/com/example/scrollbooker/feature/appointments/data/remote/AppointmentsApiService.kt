package com.example.scrollbooker.feature.appointments.data.remote

import com.example.scrollbooker.core.util.PaginatedResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AppointmentsApiService {
    @GET("users/{userId}/appointments")
    suspend fun getUserAppointments(
        @Path("userId") userId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("as_customer") asCustomer: Boolean
    ): PaginatedResponseDto<AppointmentDto>
}