package com.example.scrollbooker.entity.booking.userCalendarSettings.domain.repository

import com.example.scrollbooker.entity.booking.userCalendarSettings.domain.model.UserCalendarSettings

interface UserCalendarSettingsRepository {
    suspend fun getCalendarSettings(userId: Int): UserCalendarSettings

    suspend fun updateSlotDuration(slotDurationMinutes: Int): UserCalendarSettings

    suspend fun updateAppointmentGap(appointmentGapMinutes: Int): UserCalendarSettings
}
