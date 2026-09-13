package com.example.scrollbooker.entity.booking.userCalendarSettings.data.repository

import com.example.scrollbooker.entity.booking.userCalendarSettings.data.mappers.toDomain
import com.example.scrollbooker.entity.booking.userCalendarSettings.data.remote.AppointmentGapUpdateRequest
import com.example.scrollbooker.entity.booking.userCalendarSettings.data.remote.SlotDurationUpdateRequest
import com.example.scrollbooker.entity.booking.userCalendarSettings.data.remote.UserCalendarSettingsApiService
import com.example.scrollbooker.entity.booking.userCalendarSettings.domain.model.UserCalendarSettings
import com.example.scrollbooker.entity.booking.userCalendarSettings.domain.repository.UserCalendarSettingsRepository
import javax.inject.Inject

class UserCalendarSettingsRepositoryImpl @Inject constructor(
    private val apiService: UserCalendarSettingsApiService
): UserCalendarSettingsRepository {
    override suspend fun getCalendarSettings(userId: Int): UserCalendarSettings {
        return apiService.getCalendarSettings(userId).toDomain()
    }

    override suspend fun updateSlotDuration(slotDurationMinutes: Int): UserCalendarSettings {
        return apiService.updateSlotDuration(
            SlotDurationUpdateRequest(slotDurationMinutes = slotDurationMinutes)
        ).toDomain()
    }

    override suspend fun updateAppointmentGap(appointmentGapMinutes: Int): UserCalendarSettings {
        return apiService.updateAppointmentGap(
            AppointmentGapUpdateRequest(appointmentGapMinutes = appointmentGapMinutes)
        ).toDomain()
    }
}
