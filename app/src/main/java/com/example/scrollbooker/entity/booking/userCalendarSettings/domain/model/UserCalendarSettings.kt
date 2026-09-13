package com.example.scrollbooker.entity.booking.userCalendarSettings.domain.model

data class UserCalendarSettings(
    val userId: Int,
    val slotDurationMinutes: Int,
    val appointmentGapMinutes: Int
)
