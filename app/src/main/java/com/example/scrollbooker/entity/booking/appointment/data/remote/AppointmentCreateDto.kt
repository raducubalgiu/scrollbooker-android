package com.example.scrollbooker.entity.booking.appointment.data.remote

import com.google.gson.annotations.SerializedName
import org.threeten.bp.ZonedDateTime
import java.math.BigDecimal

data class AppointmentCreateDto(
    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("business_id")
    val businessId: Int,

    @SerializedName("customer_id")
    val customerId: Int?,

    @SerializedName("currency_id")
    val currencyId: Int,

    @SerializedName("service_id")
    val serviceId: Int,

    @SerializedName("product_id")
    val productId: Int?,

    @SerializedName("product_name")
    val productName: String,

    @SerializedName("product_full_price")
    val productPrice: BigDecimal,

    @SerializedName("product_price_with_discount")
    val productPriceWithDiscount: BigDecimal,

    @SerializedName("product_duration")
    val productDuration: Int,

    @SerializedName("product_discount")
    val productDiscount: BigDecimal,

    @SerializedName("channel")
    val channel: String,

    @SerializedName("customer_fullname")
    val customerFullName: String,
)