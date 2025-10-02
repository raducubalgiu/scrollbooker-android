package com.example.scrollbooker.entity.booking.calendar.domain.model

import com.example.scrollbooker.core.enums.AppointmentChannelEnum
import com.example.scrollbooker.core.util.parseTimeStringFromLocalDateTimeString
import com.example.scrollbooker.entity.nomenclature.currency.domain.model.Currency
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import org.threeten.bp.LocalDateTime
import java.math.BigDecimal

data class CalendarEvents(
    val minSlotTime: LocalDateTime?,
    val maxSlotTime: LocalDateTime?,
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
    val startDateLocale: LocalDateTime?,
    val endDateLocale: LocalDateTime?,
    val startDateUtc: String,
    val endDateUtc: String,
    val isBooked: Boolean,
    val isClosed: Boolean,
    val isBlocked: Boolean,
    val info: CalendarEventsInfo?
)

data class CalendarEventsInfo(
    val currency: Currency?,
    val channel: AppointmentChannelEnum?,
    val serviceName: String,
    val product: CalendarEventsProduct,
    val customer: UserSocial?,
    val message: String?
)

data class CalendarEventsProduct(
    val productName: String,
    val productFullPrice: BigDecimal,
    val productPriceWithDiscount: BigDecimal,
    val productDiscount: BigDecimal
)

fun CalendarEvents.blockedStartLocale(): Set<LocalDateTime> =
    days.asSequence()
        .flatMap { it.slots.asSequence() }
        .filter { it.isBlocked }
        .map { it.startDateLocale }
        .filterNotNull()
        .toSet()

data class SlotTimeBounds(
    val start: String,
    val end: String
)

fun CalendarEventsSlot.toTime(): SlotTimeBounds =
    SlotTimeBounds(
        start = parseTimeStringFromLocalDateTimeString(startDateLocale),
        end = parseTimeStringFromLocalDateTimeString(endDateLocale)
    )