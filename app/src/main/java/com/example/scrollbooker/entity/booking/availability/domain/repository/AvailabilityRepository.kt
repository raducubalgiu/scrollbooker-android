package com.example.scrollbooker.entity.booking.availability.domain.repository

import com.example.scrollbooker.entity.booking.availability.domain.model.AvailableDay
import com.example.scrollbooker.entity.booking.availability.domain.model.CalendarEvents
import com.example.scrollbooker.entity.booking.availability.domain.model.CalendarEventsBusinessDay
import com.example.scrollbooker.entity.booking.availability.domain.model.EmployeeAvailability

interface AvailabilityRepository {
    suspend fun getUserCalendarAvailableDays(
        businessId: Int,
        employeeId: Int?,
        startDate: String,
        endDate: String,
        slotDuration: Int
    ): List<String>

    suspend fun getUserAvailableTimeSlots(
        businessId: Int,
        employeeId: Int?,
        slotDuration: Int,
        day: String,
    ): AvailableDay

    suspend fun getUserCalendarEvents(
        businessId: Int,
        employeeId: Int?,
        startDate: String,
        endDate: String,
        slotDuration: Int
    ): CalendarEvents

    suspend fun getBusinessEmployeesCalendarEventsByDay(
        day: String,
        slotDuration: Int
    ): CalendarEventsBusinessDay

    suspend fun getEmployeesAvailabilityForDay(
        day: String,
        slotDuration: Int
    ): List<EmployeeAvailability>
}