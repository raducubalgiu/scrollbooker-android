package com.example.scrollbooker.feature.appointments.data.remote
import com.google.gson.annotations.SerializedName

data class AppointmentDto(
    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,

    val channel: String,
    val status: String,
    val product: ProductDto,
    val user: UserDto,

    @SerializedName("is_customer")
    val isCustomer: Boolean
)

data class ProductDto(
    val id: Int,
    val name: String,
    val price: Int,

    @SerializedName("price_with_discount")
    val priceWithDiscount: Int,

    val discount: Int,
    val currency: String
)

data class UserDto(
    val id: Int,
    val fullName: String,
    val username: String,
    val avatar: String,

    @SerializedName("ratings_average")
    val ratingsAverage: Double,

    val profession: String
)