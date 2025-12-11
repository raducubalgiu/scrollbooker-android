package com.example.scrollbooker.entity.booking.appointment.data.remote

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class AppointmentOwnClientCreateDto(
    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,

    @SerializedName("customer_fullname")
    val customerFullname: String,

    @SerializedName("product_name")
    val productName: String,

    val price: BigDecimal,

    @SerializedName("price_with_discount")
    val priceWithDiscount: BigDecimal,

    val discount: BigDecimal,
    val duration: Int,
)