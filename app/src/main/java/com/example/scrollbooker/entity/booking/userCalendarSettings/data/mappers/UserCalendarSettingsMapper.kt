package com.example.scrollbooker.entity.booking.userCalendarSettings.data.mappers

import com.example.scrollbooker.entity.booking.userCalendarSettings.data.remote.UserCalendarSettingsDto
import com.example.scrollbooker.entity.booking.userCalendarSettings.domain.model.UserCalendarSettings

fun UserCalendarSettingsDto.toDomain(): UserCalendarSettings = UserCalendarSettings(
    userId = userId,
    slotDurationMinutes = slotDurationMinutes,
    appointmentGapMinutes = appointmentGapMinutes
)
