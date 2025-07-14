package com.example.scrollbooker.entity.appointment.domain.model
import org.threeten.bp.ZonedDateTime
import java.math.BigDecimal

data class Appointment(
    val id: Int,
    val startDate: ZonedDateTime,
    val endDate: ZonedDateTime,
    val channel: String,
    val status: String,
    val product: AppointmentProduct,
    val user: AppointmentUser,
    val isCustomer: Boolean
)

data class AppointmentProduct(
    val id: Int?,
    val name: String,
    val price: BigDecimal,
    val priceWithDiscount: BigDecimal,
    val discount: BigDecimal,
    val currency: String,
    val exchangeRate: BigDecimal
)

data class AppointmentUser(
    val id: Int?,
    val avatar: String?,
    val fullName: String,
    val username: String?,
    val profession: String?
)