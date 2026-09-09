package com.example.scrollbooker.entity.calendar.connection.domain.repository

import com.example.scrollbooker.entity.calendar.connection.domain.model.CalendarConnection

interface CalendarConnectionRepository {
    suspend fun getCalendarConnection(businessId: Int): CalendarConnection?

    suspend fun connectGoogleCalendar(businessId: Int, serverAuthCode: String): CalendarConnection

    suspend fun disconnectCalendarConnection(businessId: Int, connectionId: Int)
}
