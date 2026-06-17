package com.example.scrollbooker.entity.booking.booking.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookingFlowApiService {
    @GET("businesses/{businessId}/booking")
    suspend fun getBookingFlow(
        @Path("businessId") businessId: Int,
        @Query("employee_id") employeeId: Int?
    ): BookingFlowDto
}