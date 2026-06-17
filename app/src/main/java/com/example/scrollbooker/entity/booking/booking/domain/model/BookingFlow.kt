package com.example.scrollbooker.entity.booking.booking.domain.model

import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.google.gson.annotations.SerializedName

data class BookingFlow(
    val business: BookingFlowBusiness,
    val products: UserProducts,
    val employees: List<BookingFlowUser>
)

data class BookingFlowBusiness(
    val owner: BookingFlowUser,

    @SerializedName("has_employees")
    val hasEmployees: Boolean,

    @SerializedName("formatted_address")
    val formattedAddress: String
)

data class BookingFlowUser(
    val id: Int,
    val username: String,

    @SerializedName("fullname")
    val fullName: String,

    val profession: String,

    val avatar: String?
)