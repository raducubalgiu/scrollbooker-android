package com.example.scrollbooker.entity.appointment.data.remote
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class AppointmentDto(
    val id: Int,

    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,

    val channel: String,
    val status: String,
    val product: AppointmentProductDto,
    val user: AppointmentUserDto,

    @SerializedName("is_customer")
    val isCustomer: Boolean
)

data class AppointmentProductDto(
    val id: Int?,
    val name: String,
    val price: BigDecimal,

    @SerializedName("price_with_discount")
    val priceWithDiscount: BigDecimal,

    val discount: BigDecimal,
    val currency: String,

    @SerializedName("exchange_rate")
    val exchangeRate: BigDecimal
)

data class AppointmentUserDto(
    val id: Int?,

    @SerializedName("fullname")
    val fullName: String,

    val username: String?,
    val avatar: String?,
    val profession: String?
)