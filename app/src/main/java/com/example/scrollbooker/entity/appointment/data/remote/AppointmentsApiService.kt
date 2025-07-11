package com.example.scrollbooker.entity.appointment.data.remote

import com.example.scrollbooker.core.util.PaginatedResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface AppointmentsApiService {
    @GET("appointments")
    suspend fun getUserAppointments(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("as_customer") asCustomer: Boolean
    ): PaginatedResponseDto<AppointmentDto>
}