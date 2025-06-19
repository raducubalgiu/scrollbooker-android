package com.example.scrollbooker.shared.appointment.domain.model

data class Appointment(
    val id: Int,
    val startDate: String,
    val endDate: String,
    val channel: String,
    val status: String,
    val product: AppointmentProduct,
    val user: AppointmentUser,
    val isCustomer: Boolean
)

data class AppointmentProduct(
    val id: Int?,
    val name: String,
    val price: Int,
    val priceWithDiscount: Int,
    val discount: Int,
    val currency: String
)

data class AppointmentUser(
    val id: Int?,
    val avatar: String?,
    val fullName: String,
    val username: String?,
    val profession: String?
)