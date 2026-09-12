package com.example.scrollbooker.entity.booking.availability.domain.model

data class CalendarEventsBusinessDay(
    val businessShortDomain: String,
    val employees: List<CalendarEventsBusinessEmployee>
)

data class CalendarEventsBusinessEmployee(
    val id: Int,
    val fullname: String,
    val username: String,
    val avatar: String?,
    val profession: String,
    val slots: List<CalendarEventsSlot>
)
