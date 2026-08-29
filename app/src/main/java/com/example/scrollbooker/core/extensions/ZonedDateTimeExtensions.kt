package com.example.scrollbooker.core.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.AppLocaleProvider
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit
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

/**
 * Compact relative label used for feed/comment timestamps (e.g. "30min", "3h", "09.03"),
 * following the Instagram/TikTok convention.
 */
@Composable
fun ZonedDateTime.relativeLabel(
    zone: ZoneId = ZoneId.systemDefault(),
    locale: Locale = AppLocaleProvider.current(),
    now: ZonedDateTime = remember { ZonedDateTime.now() },
): String {
    val minutes = ChronoUnit.MINUTES.between(this, now)
    val hours = ChronoUnit.HOURS.between(this, now)
    val days = ChronoUnit.DAYS.between(this, now)

    return when {
        minutes < 1 -> stringResource(R.string.justNow)
        minutes < 60 -> stringResource(R.string.minutesAgo, minutes)
        hours < 24 -> stringResource(R.string.hoursAgo, hours)
        days < 7 -> stringResource(R.string.daysAgo, days)
        else -> {
            val dt = toZone(zone)
            val formatter = if (dt.year == now.year) {
                DateTimeFormatters.uiDayMonthDot
            } else {
                DateTimeFormatters.uiDayMonthYearDot
            }
            dt.format(formatter.withLocale(locale))
        }
    }
}