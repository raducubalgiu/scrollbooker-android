package com.example.scrollbooker.entity.calendar.data.repository

import com.example.scrollbooker.entity.calendar.data.mappers.toDomain
import com.example.scrollbooker.entity.calendar.data.remote.CalendarApiService
import com.example.scrollbooker.entity.calendar.domain.model.AvailableDay
import com.example.scrollbooker.entity.calendar.domain.repository.CalendarRepository
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
}