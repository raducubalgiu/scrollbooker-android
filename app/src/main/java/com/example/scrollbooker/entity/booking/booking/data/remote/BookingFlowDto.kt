package com.example.scrollbooker.entity.booking.booking.data.remote

import com.example.scrollbooker.entity.booking.products.data.remote.UserProductsDto
import com.google.gson.annotations.SerializedName

data class BookingFlowDto(
    val business: BookingFlowBusinessDto,
    val products: UserProductsDto,
    val employees: List<BookingFlowUserDto>
)

data class BookingFlowBusinessDto(
    val owner: BookingFlowUserDto,

    @SerializedName("has_employees")
    val hasEmployees: Boolean,

    @SerializedName("formatted_address")
    val formattedAddress: String
)

data class BookingFlowUserDto(
    val id: Int,
    val username: String,

    @SerializedName("fullname")
    val fullName: String,

    val profession: String,

    val avatar: String?
)