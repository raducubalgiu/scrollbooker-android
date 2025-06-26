package com.example.scrollbooker.entity.schedule.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface SchedulesApiService {
    @GET("users/{userId}/schedules")
    suspend fun getSchedules(
        @Path("userId") userId: Int
    ): List<ScheduleDto>

    @PUT("schedules")
    suspend fun updateSchedules(
        @Body schedules: List<ScheduleDto>
    ): List<ScheduleDto>
}