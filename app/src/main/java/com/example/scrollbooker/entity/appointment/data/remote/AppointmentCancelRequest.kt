package com.example.scrollbooker.entity.appointment.data.remote

import com.google.gson.annotations.SerializedName

data class AppointmentCancelRequest(
    @SerializedName("appointment_id")
    val appointmentId: Int,

    val message: String
)