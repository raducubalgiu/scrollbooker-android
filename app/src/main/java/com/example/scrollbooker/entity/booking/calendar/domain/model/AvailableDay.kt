package com.example.scrollbooker.entity.booking.calendar.domain.model

data class AvailableDay(
    val isClosed: Boolean,
    val availableSlots: List<Slot>
)

data class Slot(
    val startDateUtc: String,
    val endDateUtc: String,
    val startDateLocale: String,
    val endDateLocale: String,
)
