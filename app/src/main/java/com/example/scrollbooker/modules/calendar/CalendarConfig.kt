package com.example.scrollbooker.modules.calendar
import org.threeten.bp.LocalDate

data class CalendarConfig(
    val userId: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val totalWeeks: Int,
    val initialWeekPage: Int,
    val initialDayPage: Int,
    val selectedDay: LocalDate
)
