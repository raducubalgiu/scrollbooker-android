package com.example.scrollbooker.entity.booking.calendar.domain.model

import java.math.BigDecimal

data class AvailableDay(
    val isClosed: Boolean,
    val availableSlots: List<Slot>
)

data class Slot(
    val startDateUtc: String,
    val endDateUtc: String,
    val startDateLocale: String,
    val endDateLocale: String,
    val isLastMinute: Boolean,
    val lastMinuteDiscount: BigDecimal?
)
