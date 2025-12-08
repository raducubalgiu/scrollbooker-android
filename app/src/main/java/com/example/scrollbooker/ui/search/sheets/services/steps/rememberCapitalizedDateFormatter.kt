package com.example.scrollbooker.ui.search.sheets.services.steps

import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberCapitalizedDateFormatter(): DatePickerFormatter {
    val defaultFormatter = remember { DatePickerDefaults.dateFormatter() }

    return remember {
        object : DatePickerFormatter {
            override fun formatDate(
                dateMillis: Long?,
                locale: CalendarLocale,
                forContentDescription: Boolean
            ): String? {
                return defaultFormatter.formatDate(dateMillis, locale, forContentDescription)
            }

            override fun formatMonthYear(
                monthMillis: Long?,
                locale: CalendarLocale
            ): String? {
                val original = defaultFormatter.formatMonthYear(monthMillis, locale) ?: return null
                return original.replaceFirstChar { it.uppercaseChar() }
            }
        }
    }
}