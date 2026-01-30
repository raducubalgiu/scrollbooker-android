package com.example.scrollbooker.core.extensions

import com.example.scrollbooker.core.util.AppLocaleProvider
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

private fun dayFmt(locale: Locale) = DateTimeFormatter.ofPattern("dd", locale)
private fun monthFmt(locale: Locale) = DateTimeFormatter.ofPattern("MMM", locale)
private fun yearFmt(locale: Locale) = DateTimeFormatter.ofPattern("YYYY", locale)
private fun timeFmt(locale: Locale) = DateTimeFormatter.ofPattern("HH:mm", locale)

fun ZonedDateTime.toZone(
    zone: ZoneId = ZoneId.systemDefault()
): ZonedDateTime = withZoneSameInstant(zone)

fun ZonedDateTime.time(
    zone: ZoneId = ZoneId.systemDefault(),
    locale: Locale = AppLocaleProvider.current()
): String =
    toZone(zone).format(timeFmt(locale))

fun ZonedDateTime.day(
    zone: ZoneId = ZoneId.systemDefault(),
    locale: Locale = AppLocaleProvider.current()
): String =
    toZone(zone).format(dayFmt(locale))

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
    val year = dt.format(yearFmt(locale))

    val month = dt.format(monthFmt(locale))
        .lowercase()
        .removeSuffix(".")

    val time = dt.format(timeFmt(locale))

    return "$dayOfWeek, $day $month $year $time"
}