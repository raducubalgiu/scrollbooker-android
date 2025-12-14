package com.example.scrollbooker.entity.booking.appointment.data.remote

import com.example.scrollbooker.core.util.PaginatedResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AppointmentsApiService {
    @GET("appointments")
    suspend fun getUserAppointments(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("as_customer") asCustomer: Boolean?
    ): PaginatedResponseDto<AppointmentDto>

    @GET("appointments/count")
    suspend fun getUserAppointmentsNumber(): Int

    @POST("appointments/create-scrollbooker-appointment")
    suspend fun createScrollBookerAppointment(
        @Body request: AppointmentScrollBookerCreateDto
    )

    @POST("appointments/create-own-client-appointment")
    suspend fun createOwnClientAppointment(
        @Body request: AppointmentOwnClientCreateDto
    )

    @POST("appointments/create-last-minute-appointments")
    suspend fun createLastMinuteAppointment(
        @Body request: AppointmentLastMinuteRequest
    )

    @POST("appointments/create-block-appointments")
    suspend fun blockAppointments(
        @Body request: AppointmentBlockRequest
    )

    @PUT("appointments/cancel-appointment")
    suspend fun cancelAppointment(
        @Body request: AppointmentCancelRequest
    )
}