package com.example.scrollbooker.entity.calendar.data.mappers

import com.example.scrollbooker.entity.calendar.data.remote.AvailableDayDto
import com.example.scrollbooker.entity.calendar.data.remote.SlotDto
import com.example.scrollbooker.entity.calendar.domain.model.AvailableDay
import com.example.scrollbooker.entity.calendar.domain.model.Slot

fun AvailableDayDto.toDomain(): AvailableDay {
    return AvailableDay(
        isClosed = isClosed,
        slots = slots.map { it.toDomain() }
    )
}

fun SlotDto.toDomain(): Slot {
    return Slot(
        startDateUtc = startDateUtc,
        endDateUtc = endDateUtc,
        startDateLocale = startDateLocale,
        endDateLocale = endDateLocale
    )
}