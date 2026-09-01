package com.example.scrollbooker.entity.booking.appointment.data.remote

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class AppointmentOwnClientCreateDto(
    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("customer_fullname")
    val customerFullname: String,

    @SerializedName("payment_currency_id")
    val paymentCurrencyId: Int,

    @SerializedName("custom_product")
    val customProduct: AppointmentCustomProductDto?,

    @SerializedName("product_variants")
    val productVariants: List<AppointmentProductVariantCreateDto>? = null,
)

data class AppointmentCustomProductDto(
    @SerializedName("product_name")
    val productName: String,

    val price: BigDecimal,

    @SerializedName("price_with_discount")
    val priceWithDiscount: BigDecimal,

    val discount: BigDecimal,
    val duration: Int,
)