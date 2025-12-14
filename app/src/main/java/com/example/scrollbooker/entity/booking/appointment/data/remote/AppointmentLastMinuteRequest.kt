package com.example.scrollbooker.entity.booking.appointment.data.remote

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class AppointmentLastMinuteRequest(
    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,

    @SerializedName("last_minute_discount")
    val lastMinuteDiscount: BigDecimal
)