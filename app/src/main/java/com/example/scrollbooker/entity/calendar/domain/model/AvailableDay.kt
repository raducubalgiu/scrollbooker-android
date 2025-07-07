package com.example.scrollbooker.entity.calendar.domain.model

data class AvailableDay(
    val isClosed: Boolean,
    val slots: List<Slot>
)

data class Slot(
    val startDateUtc: String,
    val endDateUtc: String,
    val startDateLocale: String,
    val endDateLocale: String,
)
