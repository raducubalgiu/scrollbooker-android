package com.example.scrollbooker.entity.booking.appointment.domain.model
import java.math.BigDecimal

data class AppointmentCreate(
    val startDate: String,
    val endDate: String,
    val userId: Int,
    val businessId: Int,
    val customerId: Int?,
    val currencyId: Int,
    val serviceId: Int,

    val productId: Int?,
    val productName: String,
    val productPrice: BigDecimal,
    val productPriceWithDiscount: BigDecimal,
    val productDuration: Int,
    val productDiscount: BigDecimal,

    val channel: String,
    val customerFullName: String,
)