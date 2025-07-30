package com.example.scrollbooker.entity.booking.appointment.domain.model
import org.threeten.bp.OffsetDateTime
import java.math.BigDecimal

data class AppointmentCreate(
    val startDate: OffsetDateTime,
    val endDate: OffsetDateTime,
    val userId: Int,
    val businessId: Int,
    val customerId: Int?,
    val currencyId: Int,
    val serviceId: Int,
    val productId: Int?,
    val channel: String,
    val fullname: String,
    val serviceName: String,
    val productName: String,
    val productPrice: BigDecimal,
    val productPriceWithDiscount: BigDecimal,
    val productDiscount: BigDecimal
)