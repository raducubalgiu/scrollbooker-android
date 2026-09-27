package com.example.scrollbooker.entity.booking.products.domain.model

data class LinkedProductsBusiness(
    val id: Int,
    val fullname: String,
    val username: String,
    val profession: String,
    val avatar: String?,
    val ratingsAverage: Float,
    val ratingsCount: Int,
    val distanceKm: Float?,
    val address: String?
)

data class LinkedProducts(
    val business: LinkedProductsBusiness,
    val products: List<Product>
)
