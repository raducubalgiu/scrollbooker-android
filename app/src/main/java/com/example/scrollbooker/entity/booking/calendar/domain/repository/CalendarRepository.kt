package com.example.scrollbooker.entity.booking.calendar.domain.repository

import com.example.scrollbooker.entity.booking.calendar.domain.model.AvailableDay

interface CalendarRepository {
    suspend fun getUserCalendarAvailableDays(userId: Int, startDate: String, endDate: String): List<String>
    suspend fun getUserAvailableTimeSlots(day: String, userId: Int, slotDuration: Int): AvailableDay
}