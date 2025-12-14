package com.example.scrollbooker.entity.booking.calendar.data.mappers

import com.example.scrollbooker.core.enums.AppointmentChannelEnum
import com.example.scrollbooker.core.enums.BusinessShortDomainEnum
import com.example.scrollbooker.core.extensions.parseDateTimeStringToLocalDateTime
import com.example.scrollbooker.entity.booking.calendar.data.remote.CalendarEventsCustomerDto
import com.example.scrollbooker.entity.booking.calendar.data.remote.CalendarEventsDayDto
import com.example.scrollbooker.entity.booking.calendar.data.remote.CalendarEventsDto
import com.example.scrollbooker.entity.booking.calendar.data.remote.CalendarEventsInfoDto
import com.example.scrollbooker.entity.booking.calendar.data.remote.CalendarEventsSlotDto
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEvents
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsCustomer
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsDay
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsInfo
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsSlot

fun CalendarEventsDto.toDomain(): CalendarEvents {
    return CalendarEvents(
        minSlotTime = parseDateTimeStringToLocalDateTime(minSlotTime),
        maxSlotTime = parseDateTimeStringToLocalDateTime(maxSlotTime),
        businessShortDomain = BusinessShortDomainEnum.fromKeyOrUnknown(businessShortDomain),
        days = days.map { it.toDomain() }
    )
}

fun CalendarEventsDayDto.toDomain(): CalendarEventsDay {
    return CalendarEventsDay(
        day = day,
        isBooked = isBooked,
        isClosed = isClosed,
        slots = slots.map { it.toDomain() }
    )
}

fun CalendarEventsSlotDto.toDomain(): CalendarEventsSlot {
    return CalendarEventsSlot(
        id = id,
        startDateLocale = parseDateTimeStringToLocalDateTime(startDateLocale),
        endDateLocale = parseDateTimeStringToLocalDateTime(endDateLocale),
        startDateUtc = startDateUtc,
        endDateUtc = endDateUtc,
        isBooked = isBooked,
        isBlocked = isBlocked,
        isLastMinute = isLastMinute,
        lastMinuteDiscount = lastMinuteDiscount,
        info = info?.toDomain()
    )
}

fun CalendarEventsCustomerDto.toDomain(): CalendarEventsCustomer {
    return CalendarEventsCustomer(
        id = id,
        fullname = fullname,
        username = username,
        avatar = avatar
    )
}

fun CalendarEventsInfoDto.toDomain(): CalendarEventsInfo {
    return CalendarEventsInfo(
        channel = AppointmentChannelEnum.fromKey(channel),
        customer = customer,
        message = message,
        totalPrice = totalPrice,
        totalPriceWithDiscount = totalPriceWithDiscount,
        totalDiscount = totalDiscount,
        totalDuration = totalDuration,
        paymentCurrency = paymentCurrency
    )
}