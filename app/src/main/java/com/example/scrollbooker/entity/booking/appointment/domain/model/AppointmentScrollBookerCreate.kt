package com.example.scrollbooker.entity.booking.appointment.domain.model

data class AppointmentScrollBookerCreate(
    val startDate: String,
    val endDate: String,
    val userId: Int,
    val serviceId: Int,
    val productId: Int,
    val currencyId: Int,
)