package com.example.scrollbooker.entity.dashboard.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface DashboardApiService {
    @GET("dashboard/bookings")
    suspend fun getDashboardBooking(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
    ): DashboardBookingDto
}