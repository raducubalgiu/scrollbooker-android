package com.example.scrollbooker.feature.myBusiness.data.remote.schedules

import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface SchedulesApiService {
    @GET("users/{userId}/schedules")
    suspend fun getSchedules(
        @Path("userId") userId: Int
    ): List<ScheduleDto>

    @PUT("businesses/{businessId}/schedules")
    suspend fun updateSchedules(
        @Path("businessId") businessId: Int
    )
}