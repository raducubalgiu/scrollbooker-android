import com.example.scrollbooker.core.extensions.DateTimeFormatters
import com.example.scrollbooker.core.util.AppLocaleProvider
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.TextStyle

fun LocalDate.toIsoString(): String {
    return this.format(DateTimeFormatters.apiDate)
}

fun LocalDate.toDayMonthShort(): String =
    this.format(DateTimeFormatters.uiDayMonthShort).replace(".", "")

fun LocalDate.toPrettyDate(): String {
    val locale = AppLocaleProvider.current()

    val dayName = this.dayOfWeek
        .getDisplayName(TextStyle.FULL, locale)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }

    val monthShort = this.month
        .getDisplayName(TextStyle.SHORT, locale)
        .lowercase(locale)
        .replace(".", "")
        .replaceFirstChar { it.lowercase(locale) }

    return "$dayName ${this.dayOfMonth} $monthShort"
}

fun LocalDate.toPrettyFullDate(): String {
    val locale = AppLocaleProvider.current()

    val dayName = this.dayOfWeek
        .getDisplayName(TextStyle.FULL, locale)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }

    val monthShort = this.month
        .getDisplayName(TextStyle.SHORT, locale)
        .lowercase(locale)
        .replace(".", "")
        .replaceFirstChar { it.lowercase(locale) }

    val year = this.year

    return "$dayName, ${this.dayOfMonth} $monthShort $year"
}

fun LocalDate.toUtcEpochMillis(): Long =
    atStartOfDay(ZoneOffset.UTC)
        .toInstant()
        .toEpochMilli()