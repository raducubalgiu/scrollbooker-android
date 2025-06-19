package com.example.scrollbooker.screens.calendar.components.util

import org.threeten.bp.LocalDate

fun generateCalendarDays(startDate: LocalDate): List<LocalDate> {
    val endDate = startDate.plusMonths(3)
    val days = mutableListOf<LocalDate>()
    var current = startDate.withDayOfMonth(1)

    while (current.isBefore(endDate)) {
        val monthLength = current.lengthOfMonth()
        repeat(monthLength) {
            days.add(current.withDayOfMonth(it + 1))
        }
        current = current.plusMonths(1)
    }

    return days
}