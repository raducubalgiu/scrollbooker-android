package com.example.scrollbooker.entity.booking.appointment.domain.model
import java.math.BigDecimal

data class AppointmentOwnClientCreate(
    val startDate: String,
    val endDate: String,
    val userId: Int,
    val customerFullname: String,
    val paymentCurrencyId: Int = 1,
    val customProduct: AppointmentCustomProduct,
)

data class AppointmentCustomProduct(
    val productName: String,
    val price: BigDecimal,
    val priceWithDiscount: BigDecimal,
    val discount: BigDecimal,
    val duration: Int,
)