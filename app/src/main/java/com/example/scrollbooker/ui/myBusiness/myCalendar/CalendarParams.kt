package com.example.scrollbooker.ui.myBusiness.myCalendar

import org.threeten.bp.LocalDate

data class CalendarParams(
    val userId: Int,
    val businessId: Int,
    val employeeId: Int?,
    val day: LocalDate,
    val slot: Int,
    val refresh: Int
)