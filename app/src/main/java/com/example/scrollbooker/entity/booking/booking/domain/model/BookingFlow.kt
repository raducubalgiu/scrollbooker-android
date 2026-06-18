package com.example.scrollbooker.entity.booking.booking.domain.model

import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts

data class BookingFlow(
    val business: BookingFlowBusiness,
    val products: UserProducts,
    val employees: List<BookingFlowUser>
)

data class BookingFlowBusiness(
    val owner: BookingFlowUser,
    val hasEmployees: Boolean,
    val formattedAddress: String
)

data class BookingFlowUser(
    val id: Int,
    val username: String,
    val fullName: String,
    val profession: String,
    val avatar: String?,
    val ratingsCount: Int,
    val ratingsAverage: Float
)