package com.example.scrollbooker.core.extensions

import com.example.scrollbooker.core.util.AppLocaleProvider
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeFormatterBuilder
import org.threeten.bp.format.TextStyle
import org.threeten.bp.temporal.ChronoField

object DateTimeFormatters {
    val apiDate: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    val uiTime: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val uiDayMonthShort: DateTimeFormatter by lazy {
        DateTimeFormatterBuilder()
            .appendValue(ChronoField.DAY_OF_MONTH)
            .appendLiteral(' ')
            .appendText(
                ChronoField.MONTH_OF_YEAR,
                TextStyle.SHORT_STANDALONE
            )
            .toFormatter(AppLocaleProvider.current())
    }
}