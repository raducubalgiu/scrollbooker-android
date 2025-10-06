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

    @SerializedName("service_id")
    val serviceId: Int?,

    @SerializedName("service_name")
    val serviceName: String,

    @SerializedName("product_id")
    val productId: Int?,

    @SerializedName("product_name")
    val productName: String,

    @SerializedName("product_full_price")
    val productPrice: BigDecimal,

    @SerializedName("product_price_with_discount")
    val productPriceWithDiscount: BigDecimal,

    @SerializedName("product_discount")
    val productDiscount: BigDecimal,

    @SerializedName("product_duration")
    val productDuration: Int,

    @SerializedName("currency_id")
    val currencyId: Int,
)