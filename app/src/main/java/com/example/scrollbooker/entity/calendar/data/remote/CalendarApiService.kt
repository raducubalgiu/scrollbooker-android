package com.example.scrollbooker.entity.calendar.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface CalendarApiService {
    @GET("appointments/calendar-available-days")
    suspend fun getUserCalendarAvailableDays(
        @Query("user_id") userId: Int,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): List<String>

    @GET("appointments/timeslots")
    suspend fun getUserAvailableTimeslots(
        @Query("day") day: String,
        @Query("user_id") userId: Int,
        @Query("slot_duration") slotDuration: Int
    ): AvailableDayDto
}