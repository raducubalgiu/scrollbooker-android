package com.example.scrollbooker.entity.booking.appointment.data.remote

import com.google.gson.annotations.SerializedName

data class AppointmentCancelRequest(
    @SerializedName("canceled_reason")
    val canceledReason: String,

    @SerializedName("canceled_by_user_id")
    val canceledByUserId: Int
)