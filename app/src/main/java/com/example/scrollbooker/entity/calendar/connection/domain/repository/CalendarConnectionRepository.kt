package com.example.scrollbooker.entity.calendar.connection.domain.repository

import com.example.scrollbooker.entity.calendar.connection.domain.model.CalendarConnection

interface CalendarConnectionRepository {
    suspend fun getCalendarConnection(): CalendarConnection?

    suspend fun connectGoogleCalendar(serverAuthCode: String): CalendarConnection

    suspend fun disconnectCalendarConnection()
}
