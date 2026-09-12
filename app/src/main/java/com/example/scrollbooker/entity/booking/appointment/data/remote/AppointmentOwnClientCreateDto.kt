package com.example.scrollbooker.entity.booking.appointment.data.remote

import com.google.gson.annotations.SerializedName

data class AppointmentOwnClientCreateDto(
    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("business_client_id")
    val businessClientId: Int,

    @SerializedName("payment_currency_id")
    val paymentCurrencyId: Int,

    @SerializedName("product_variants")
    val productVariants: List<AppointmentProductVariantCreateDto>,
)
