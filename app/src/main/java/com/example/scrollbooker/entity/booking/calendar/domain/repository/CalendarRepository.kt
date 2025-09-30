package com.example.scrollbooker.entity.booking.calendar.domain.repository

import com.example.scrollbooker.entity.booking.calendar.domain.model.AvailableDay
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEvents

interface CalendarRepository {
    suspend fun getUserCalendarAvailableDays(userId: Int, startDate: String, endDate: String): List<String>
    suspend fun getUserAvailableTimeSlots(day: String, userId: Int, slotDuration: Int): AvailableDay
    suspend fun getUserCalendarEvents(startDate: String, endDate: String, userId: Int, slotDuration: Int): CalendarEvents
}