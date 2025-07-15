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
    val isCustomer: Boolean,
    val business: AppointmentBusiness
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

data class BusinessCoordinates(
    val lat: Float,
    val lng: Float
)

data class AppointmentBusiness(
    val address: String,
    val coordinates: BusinessCoordinates
)