package com.example.scrollbooker.ui.sharedModules.calendar
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

data class CalendarHeaderState(
    val config: CalendarConfig,
    val calendarDays: List<LocalDate>,
    val calendarAvailableDays: List<LocalDate>
)

data class SlotsParams(
    val day: LocalDate,
    val userId: Int,
    val slotDuration: Int,
    val forceRefresh: Boolean
)