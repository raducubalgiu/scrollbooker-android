package com.example.scrollbooker.components.customized.calendar

data class CalendarContext(
    val userId: Int,
    val businessId: Int,
    val employeeId: Int?,
    val slotDuration: Int
)
