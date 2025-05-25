package com.example.scrollbooker.feature.appointments.domain.model

data class Appointment(
    val startDate: String,
    val endDate: String,
    val channel: String,
    val status: String,
    val product: Product,
    val user: User,
    val isCustomer: Boolean
)

data class Product(
    val id: Int,
    val name: String,
    val price: Int,
    val priceWithDiscount: Int,
    val discount: Int,
    val currency: String
)

data class User(
    val id: Int,
    val avatar: String,
    val fullName: String,
    val username: String,
    val ratingsAverage: Double,
    val profession: String
)