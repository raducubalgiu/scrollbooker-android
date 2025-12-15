package com.example.scrollbooker.ui.myBusiness.myCalendar.util

import org.threeten.bp.LocalTime

fun generateTicks(
    start: LocalTime,
    end: LocalTime,
    stepMinutes: Int
): List<LocalTime> {
    require(stepMinutes > 0)

    val result = mutableListOf<LocalTime>()
    var t = start
    while (t <= end) {
        result += t
        t = t.plusMinutes(stepMinutes.toLong())
    }
    return result
}