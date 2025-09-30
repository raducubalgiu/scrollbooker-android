package com.example.scrollbooker.entity.booking.calendar.data.repository

import com.example.scrollbooker.entity.booking.calendar.data.mappers.toDomain
import com.example.scrollbooker.entity.booking.calendar.data.remote.CalendarApiService
import com.example.scrollbooker.entity.booking.calendar.domain.model.AvailableDay
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEvents
import com.example.scrollbooker.entity.booking.calendar.domain.repository.CalendarRepository
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val apiService: CalendarApiService
): CalendarRepository {
    override suspend fun getUserCalendarAvailableDays(
        userId: Int,
        startDate: String,
        endDate: String
    ): List<String> {
        return apiService.getUserCalendarAvailableDays(userId, startDate, endDate)
    }

    override suspend fun getUserAvailableTimeSlots(
        day: String,
        userId: Int,
        slotDuration: Int
    ): AvailableDay {
        return apiService.getUserAvailableTimeslots(day, userId, slotDuration).toDomain()
    }

    override suspend fun getUserCalendarEvents(
        startDate: String,
        endDate: String,
        userId: Int,
        slotDuration: Int
    ): CalendarEvents {
        return apiService.getUserCalendarEvents(startDate, endDate, userId, slotDuration).toDomain()
    }
}