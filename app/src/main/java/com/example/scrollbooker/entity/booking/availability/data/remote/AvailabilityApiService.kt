package com.example.scrollbooker.entity.booking.availability.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AvailabilityApiService {
    @GET("businesses/{businessId}/availability")
    suspend fun getUserCalendarAvailableDays(
        @Path("businessId") businessId: Int,
        @Query("employee_id") employeeId: Int?,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("slot_duration") slotDuration: Int
    ): List<String>

    @GET("businesses/{businessId}/availability/timeslots")
    suspend fun getUserAvailableTimeslots(
        @Path("businessId") businessId: Int,
        @Query("employee_id") employeeId: Int?,
        @Query("day") day: String,
        @Query("slot_duration") slotDuration: Int
    ): AvailableDayDto

    @GET("availability/{businessId}/calendar-events")
    suspend fun getUserCalendarEvents(
        @Path("businessId") businessId: Int,
        @Query("employee_id") employeeId: Int?,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("slot_duration") slotDuration: Int
    ): CalendarEventsDto
}