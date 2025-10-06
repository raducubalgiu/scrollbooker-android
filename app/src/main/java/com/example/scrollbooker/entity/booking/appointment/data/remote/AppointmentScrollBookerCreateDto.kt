package com.example.scrollbooker.entity.booking.appointment.data.remote

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class AppointmentScrollBookerCreateDto(
    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("service_id")
    val serviceId: Int,

    @SerializedName("product_id")
    val productId: Int,

    @SerializedName("currency_id")
    val currencyId: Int,
)