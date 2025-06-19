package com.example.scrollbooker.screens.calendar.components.util

import org.threeten.bp.YearMonth
import kotlin.math.ceil

fun getWeeksInMonth(month: YearMonth): Int {
    val firstDay = month.atDay(1).dayOfWeek.value % 7
    val daysInMonth = month.lengthOfMonth()
    val totalSlots = firstDay + daysInMonth
    return ceil(totalSlots / 7.0).toInt()
}