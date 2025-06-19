package com.example.scrollbooker.screens.calendar.components.util

import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth

data class CalendarMonth(
    val name: String,
    val days: List<LocalDate?>
)

fun CalendarMonth.toYearMonth(): YearMonth {
    val firstValidDate = days.firstOrNull() { it != null }
        ?: throw IllegalStateException("No valid date found in month")
    return YearMonth.from(firstValidDate)
}

fun generateCalendarMonths (start: LocalDate, months: Int = 3): List<CalendarMonth> {
    val result = mutableListOf<CalendarMonth>()
    var current = start.withDayOfMonth(1)

    repeat(months) {
        val yearMonth = YearMonth.from(current)
        val daysInMonth = (1..yearMonth.lengthOfMonth()).map { day ->
            current.withDayOfMonth(day)
        }

        val firstDayOfWeek = daysInMonth.first().dayOfWeek
        val leadingEmptySlots = (if(firstDayOfWeek == DayOfWeek.MONDAY) 0 else (firstDayOfWeek.value + 6) % 7)

        val paddedDays = List(leadingEmptySlots) { null } + daysInMonth

        result.add(
            CalendarMonth(
                name = yearMonth.month.name.lowercase(),
                days = paddedDays
            )
        )
        current = current.plusMonths(1)
    }

    return result
}