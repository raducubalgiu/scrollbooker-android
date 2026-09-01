package com.example.scrollbooker.entity.booking.appointment.data.remote

import com.google.gson.annotations.SerializedName

data class AppointmentBlockRequest(
    @SerializedName("blocked_message")
    val blockedMessage: String?,

    val slots: List<AppointmentBlockSlots>
)

data class AppointmentBlockSlots(
    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,

    @SerializedName("user_id")
    val userId: Int,
)