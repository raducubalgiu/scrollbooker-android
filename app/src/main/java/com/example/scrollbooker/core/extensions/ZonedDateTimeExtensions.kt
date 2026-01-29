package com.example.scrollbooker.core.extensions

import com.example.scrollbooker.core.util.AppLocaleProvider
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

private val DAY_FMT = DateTimeFormatter.ofPattern("dd")
private val TIME_FMT = DateTimeFormatter.ofPattern("HH:mm")
private val YEAR_FMT = DateTimeFormatter.ofPattern("YYYY")

private fun monthFmt(locale: Locale) = DateTimeFormatter.ofPattern("MMM", locale)

fun ZonedDateTime.toZone(
    zone: ZoneId = ZoneId.systemDefault()
): ZonedDateTime = withZoneSameInstant(zone)

fun ZonedDateTime.time(
    zone: ZoneId = ZoneId.systemDefault()
): String =
    toZone(zone).format(TIME_FMT)

fun ZonedDateTime.day(
    zone: ZoneId = ZoneId.systemDefault()
): String =
    toZone(zone).format(DAY_FMT)

fun ZonedDateTime.month(
    zone: ZoneId = ZoneId.systemDefault(),
    locale: Locale = AppLocaleProvider.current()
): String =
    toZone(zone)
        .format(monthFmt(locale))
        .replaceFirstChar { it.uppercase() }

fun ZonedDateTime.display(
    zone: ZoneId = ZoneId.systemDefault(),
    locale: Locale = AppLocaleProvider.current(),
): String {
    val dt = toZone(zone)

    val dayOfWeek = dt.format(DateTimeFormatter.ofPattern("EEE", locale))
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
        .removeSuffix(".")

    val day = dt.format(DateTimeFormatter.ofPattern("d", locale)).removeSuffix(".")
    val year = dt.format(DateTimeFormatter.ofPattern("YYYY", locale))

    val month = dt.format(monthFmt(locale))
        .lowercase()
        .removeSuffix(".")

    val time = dt.format(TIME_FMT)

    return "$dayOfWeek, $day $month $year $time"
}