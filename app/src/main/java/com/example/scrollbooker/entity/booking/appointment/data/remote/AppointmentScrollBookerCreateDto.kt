package com.example.scrollbooker.entity.booking.appointment.data.remote

import com.google.gson.annotations.SerializedName

data class AppointmentScrollBookerCreateDto(
    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,

    @SerializedName("product_variants")
    val productVariants: List<AppointmentProductVariantCreateDto>,

    @SerializedName("payment_currency_id")
    val paymentCurrencyId: Int,
)

data class AppointmentProductVariantCreateDto(
    val id: Int,
    val offering: AppointmentProductOfferingCreateDto
)

data class AppointmentProductOfferingCreateDto(
    @SerializedName("user_id")
    val userId: Int
)