package com.example.scrollbooker.entity.booking.appointment.data.remote

import com.google.gson.annotations.SerializedName

data class AppointmentScrollBookerCreateDto(
    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("product_ids")
    val productIds: List<Int>,

    @SerializedName("payment_currency_id")
    val currencyId: Int,
)