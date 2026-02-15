package com.example.scrollbooker.components.customized.Picker

import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.temporal.WeekFields
import java.util.Locale

object PickerUtils {
    fun getWeekDays(locale: Locale): List<DayOfWeek> {
        val firstDayOfWeek = WeekFields.of(locale).firstDayOfWeek

        return generateSequence(firstDayOfWeek) { day ->
            DayOfWeek.of((day.value % 7) + 1)
        }.take(7).toList()
    }

    fun generateMonthDays(
        month: YearMonth,
        locale: Locale
    ): List<LocalDate?> {

        val firstDayOfWeek = WeekFields.of(locale).firstDayOfWeek
        val firstOfMonth = month.atDay(1)

        val shift = (
                (firstOfMonth.dayOfWeek.value - firstDayOfWeek.value + 7) % 7
                )

        val totalCells = 42
        val days = mutableListOf<LocalDate?>()

        repeat(shift) { days.add(null) }

        for (day in 1..month.lengthOfMonth()) {
            days.add(month.atDay(day))
        }

        while (days.size < totalCells) {
            days.add(null)
        }

        return days
    }
}