package com.example.scrollbooker.entity.booking.calendar.domain.model

import com.example.scrollbooker.core.enums.AppointmentChannelEnum
import com.example.scrollbooker.core.extensions.parseTimeStringFromLocalDateTimeString
import com.example.scrollbooker.entity.nomenclature.currency.domain.model.Currency
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
    val isBlocked: Boolean,
    val isLastMinute: Boolean,
    val lastMinuteDiscount: BigDecimal?,
    val info: CalendarEventsInfo?
)

data class CalendarEventsCustomer(
    val id: Int?,
    val fullname: String,
    val username: String?,
    val avatar: String?
)

data class CalendarEventsInfo(
    val channel: AppointmentChannelEnum?,
    val customer: CalendarEventsCustomer?,
    val message: String?,
    val totalPrice: BigDecimal,
    val totalPriceWithDiscount: BigDecimal,
    val totalDiscount: BigDecimal,
    val totalDuration: Int,
    val paymentCurrency: Currency,
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