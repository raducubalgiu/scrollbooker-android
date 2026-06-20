package com.example.scrollbooker.entity.booking.availability.domain.repository

import com.example.scrollbooker.entity.booking.availability.domain.model.AvailableDay
import com.example.scrollbooker.entity.booking.availability.domain.model.CalendarEvents

interface AvailabilityRepository {
    suspend fun getUserCalendarAvailableDays(
        businessId: Int,
        employeeId: Int?,
        startDate: String,
        endDate: String
    ): List<String>

    suspend fun getUserAvailableTimeSlots(
        businessId: Int,
        employeeId: Int?,
        slotDuration: Int,
        day: String,
    ): AvailableDay

    suspend fun getUserCalendarEvents(
        startDate: String,
        endDate: String,
        userId: Int,
        slotDuration: Int
    ): CalendarEvents
}