package com.example.scrollbooker.core.util

import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber

fun formatTime(
    timeString: String?,
    inputPattern: String = "HH:mm:ss",
    outputPattern: String = "HH:mm"
): String? {
    return try {
        if(timeString.isNullOrBlank()) return null

        val inputFormatter = DateTimeFormatter.ofPattern(inputPattern)
        val outputFormatter = DateTimeFormatter.ofPattern(outputPattern)

        val time = LocalTime.parse(timeString, inputFormatter)
        time.format(outputFormatter)

    } catch (e: Exception) {
        Timber.tag("Format Time").e("ERROR: on Converting Time $e")
        timeString
    }
}