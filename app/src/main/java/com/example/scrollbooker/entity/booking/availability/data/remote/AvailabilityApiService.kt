package com.example.scrollbooker.entity.booking.availability.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface AvailabilityApiService {
    @GET("availability")
    suspend fun getUserCalendarAvailableDays(
        @Query("user_id") userId: Int,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): List<String>

    @GET("availability/timeslots")
    suspend fun getUserAvailableTimeslots(
        @Query("day") day: String,
        @Query("user_id") userId: Int,
        @Query("slot_duration") slotDuration: Int
    ): AvailableDayDto

    @GET("availability/calendar-events")
    suspend fun getUserCalendarEvents(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("user_id") userId: Int,
        @Query("slot_duration") slotDuration: Int,
    ): CalendarEventsDto
}