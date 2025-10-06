package com.example.scrollbooker.entity.booking.appointment.data.remote

import com.google.gson.annotations.SerializedName

data class AppointmentBlockRequest(
    val message: String,
    val slots: List<AppointmentBlockSlots>
)

data class AppointmentBlockSlots(
    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,
)