package com.example.scrollbooker.entity.booking.calendar.domain.model

import com.example.scrollbooker.core.enums.AppointmentChannelEnum
import com.example.scrollbooker.core.util.parseLocalTimeFromLocalDateTimeString
import com.example.scrollbooker.core.util.parseLocalTimeFromTimeString
import com.example.scrollbooker.entity.nomenclature.currency.domain.model.Currency
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import org.threeten.bp.LocalTime
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
    val channel: AppointmentChannelEnum?,
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

data class SlotTimeBounds(
    val startTime: LocalTime,
    val endTime: LocalTime
)

data class CalendarEventsTimeBounds(
    val minSlotTime: LocalTime,
    val maxSlotTime: LocalTime
)

fun CalendarEvents.timeFromLocale(): CalendarEventsTimeBounds? =
    if(minSlotTime != null && maxSlotTime != null) {
        CalendarEventsTimeBounds(
            minSlotTime = parseLocalTimeFromTimeString(minSlotTime),
            maxSlotTime = parseLocalTimeFromTimeString(maxSlotTime)
        )
    } else { null }

fun CalendarEventsSlot.timeFromLocale(): SlotTimeBounds =
    SlotTimeBounds(
        startTime = parseLocalTimeFromLocalDateTimeString(startDateLocale),
        endTime = parseLocalTimeFromLocalDateTimeString(endDateLocale)
    )