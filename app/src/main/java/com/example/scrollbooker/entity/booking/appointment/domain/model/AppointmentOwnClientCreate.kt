package com.example.scrollbooker.entity.booking.appointment.domain.model
import java.math.BigDecimal

data class AppointmentOwnClientCreate(
    val startDate: String,
    val endDate: String,
    val customerFullname: String,
    val productName: String,
    val price: BigDecimal,
    val priceWithDiscount: BigDecimal,
    val discount: BigDecimal,
    val duration: Int,
)