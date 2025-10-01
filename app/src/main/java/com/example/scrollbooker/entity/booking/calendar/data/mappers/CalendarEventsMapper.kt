package com.example.scrollbooker.entity.booking.calendar.data.mappers

import com.example.scrollbooker.core.enums.AppointmentChannelEnum
import com.example.scrollbooker.entity.booking.calendar.data.remote.CalendarEventsDayDto
import com.example.scrollbooker.entity.booking.calendar.data.remote.CalendarEventsDto
import com.example.scrollbooker.entity.booking.calendar.data.remote.CalendarEventsInfoDto
import com.example.scrollbooker.entity.booking.calendar.data.remote.CalendarEventsProductDto
import com.example.scrollbooker.entity.booking.calendar.data.remote.CalendarEventsSlotDto
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEvents
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsDay
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsInfo
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsProduct
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsSlot
import com.example.scrollbooker.entity.user.userSocial.data.mappers.toDomain

fun CalendarEventsDto.toDomain(): CalendarEvents {
    return CalendarEvents(
        minSlotTime = minSlotTime,
        maxSlotTime = maxSlotTime,
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
        startDateLocale = startDateLocale,
        endDateLocale = endDateLocale,
        startDateUtc = startDateUtc,
        endDateUtc = endDateUtc,
        isBooked = isBooked,
        isClosed = isClosed,
        isBlocked = isBlocked,
        info = info?.toDomain()
    )
}

fun CalendarEventsInfoDto.toDomain(): CalendarEventsInfo {
    return CalendarEventsInfo(
        currency = currency,
        channel = AppointmentChannelEnum.fromKey(channel),
        serviceName = serviceName,
        product = product.toDomain(),
        customer = customer.toDomain(),
        message = message
    )
}

fun CalendarEventsProductDto.toDomain(): CalendarEventsProduct {
    return CalendarEventsProduct(
        productName = productName,
        productFullPrice = productFullPrice,
        productPriceWithDiscount = productPriceWithDiscount,
        productDiscount = productDiscount
    )
}