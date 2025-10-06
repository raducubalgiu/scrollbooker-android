package com.example.scrollbooker.entity.booking.appointment.domain.model
import java.math.BigDecimal

data class AppointmentOwnClientCreate(
    val startDate: String,
    val endDate: String,
    val customerFullname: String,
    val serviceId: Int?,
    val serviceName: String,
    val productId: Int?,
    val productName: String,
    val productPrice: BigDecimal,
    val productPriceWithDiscount: BigDecimal,
    val productDiscount: BigDecimal,
    val productDuration: Int,
    val currencyId: Int,
)