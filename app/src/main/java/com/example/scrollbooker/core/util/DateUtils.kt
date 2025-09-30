package com.example.scrollbooker.core.util

import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeFormatterBuilder
import org.threeten.bp.format.TextStyle
import timber.log.Timber
import java.util.Locale

private val ISO_LOCAL: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

// "2025-09-30T15:30:00" -> LocalTime(15:30)
fun parseLocalTimeFromLocalDateTimeString(value: String): LocalTime =
    LocalDateTime.parse(value, ISO_LOCAL).toLocalTime()

// "09:30:00" or "09:30" -> LocalTime(09:30)
fun parseLocalTimeFromTimeString(value: String): LocalTime {
    val formatter = DateTimeFormatterBuilder()
        .appendPattern("HH:mm")
        .optionalStart()
        .appendPattern(":ss")
        .optionalEnd()
        .toFormatter()
    return LocalTime.parse(value, formatter)
}

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

fun displayShortDayOfWeek(date: LocalDate, locale: Locale = Locale("en")): String {
    return date.dayOfWeek.getDisplayName(TextStyle.SHORT, locale)
        .replaceFirstChar {
            if(it.isLowerCase()) it.titlecase(locale) else it.toString()
        }
}

fun displayDatePeriod(start: LocalDate, end: LocalDate, locale: Locale = Locale("en")): String {
    val startDay = start.dayOfMonth.toString()
    val endDay = end.dayOfMonth.toString()

    return if (start.month == end.month) {
        val month = start.month.getDisplayName(TextStyle.FULL, locale)
            .replace(".", "")
                .replaceFirstChar { it.uppercase(locale) }

        "$startDay - $endDay $month"
    } else {
        val startMonth = start.month.getDisplayName(TextStyle.SHORT, locale)
            .replace(".", "")
            .replaceFirstChar { it.uppercase(locale) }

        val endMonth = end.month.getDisplayName(TextStyle.SHORT, locale)
            .replace(".", "")
            .replaceFirstChar { it.uppercase(locale) }

        "$startDay $startMonth - $endDay $endMonth"
    }
}

fun formatHour(timeString: String): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val dateTime = LocalDateTime.parse(timeString)
    return dateTime.toLocalTime().format(formatter)
}

fun minutesBetween(a: LocalTime, b: LocalTime): Int =
    Duration.between(a, b).toMinutes().toInt()