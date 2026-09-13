package com.example.scrollbooker.entity.booking.userCalendarSettings.data.remote

import com.google.gson.annotations.SerializedName

data class UserCalendarSettingsDto(
    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("slot_duration_minutes")
    val slotDurationMinutes: Int,

    @SerializedName("appointment_gap_minutes")
    val appointmentGapMinutes: Int
)

data class SlotDurationUpdateRequest(
    @SerializedName("slot_duration_minutes")
    val slotDurationMinutes: Int
)

data class AppointmentGapUpdateRequest(
    @SerializedName("appointment_gap_minutes")
    val appointmentGapMinutes: Int
)
