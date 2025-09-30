package com.example.scrollbooker.entity.booking.calendar.domain.model

import com.example.scrollbooker.entity.nomenclature.currency.domain.model.Currency
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import java.math.BigDecimal

data class CalendarEvents(
    val minSlotTime: String?,
    val maxSlotTime: String?,
    val days: List<CalendarEventsDay>
)

data class CalendarEventsDay(
    val day: String,
    val isBooked: Boolean,
    val isClosed: Boolean,
    val slots: List<CalendarEventsSlot>
)

data class CalendarEventsSlot(
    val id: Int?,
    val startDateLocale: String,
    val endDateLocale: String,
    val startDateUtc: String,
    val endDateUtc: String,
    val isBooked: Boolean,
    val isClosed: Boolean,
    val isBlocked: Boolean,
    val info: CalendarEventsInfo?
)

data class CalendarEventsInfo(
    val currency: Currency,
    val channel: String,
    val serviceName: String,
    val product: CalendarEventsProduct,
    val customer: UserSocial,
    val message: String?
)

data class CalendarEventsProduct(
    val productName: String,
    val productFullPrice: BigDecimal,
    val productPriceWithDiscount: BigDecimal,
    val productDiscount: BigDecimal
)