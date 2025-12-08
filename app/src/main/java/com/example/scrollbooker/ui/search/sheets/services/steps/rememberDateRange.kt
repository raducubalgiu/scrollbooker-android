package com.example.scrollbooker.ui.search.sheets.services.steps

import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberDateRange(
    startDate: LocalDate?,
    endDate: LocalDate?
): DateRangePickerState {
    val utcZone = ZoneOffset.UTC
    val today = remember { LocalDate.now() }

    val todayStartUtc = remember {
        today.atStartOfDay(utcZone)
            .toInstant()
            .toEpochMilli()
    }
    val sixMonthsLaterStartUtc = remember {
        today.plusMonths(6)
            .atStartOfDay(utcZone)
            .toInstant()
            .toEpochMilli()
    }
    val yearRange = remember {
        val startYear = LocalDate.now().year
        val endYear = LocalDate.now().plusMonths(6).year
        startYear..endYear
    }

    val initialMonthMillis = remember(startDate) {
        val baseDate = startDate ?: today
        baseDate.atStartOfDay(utcZone)
            .toInstant()
            .toEpochMilli()
    }

    val initialSelectedStartMillis = remember(startDate) {
        startDate?.atStartOfDay(utcZone)
            ?.toInstant()
            ?.toEpochMilli()
    }

    val initialSelectedEndMillis = remember(endDate) {
        endDate?.atStartOfDay(utcZone)
            ?.toInstant()
            ?.toEpochMilli()
    }

    return rememberDateRangePickerState(
        initialDisplayedMonthMillis = initialMonthMillis,
        initialSelectedStartDateMillis = initialSelectedStartMillis,
        initialSelectedEndDateMillis = initialSelectedEndMillis,
        yearRange = yearRange,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= todayStartUtc &&
                        utcTimeMillis <= sixMonthsLaterStartUtc
            }
        },
    )
}