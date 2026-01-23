package com.example.scrollbooker.core.extensions

import com.example.scrollbooker.core.util.AppLocaleProvider
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

fun BigDecimal.toTwoDecimals(
    locale: Locale = AppLocaleProvider.current()
): String {
    val scaled = this.setScale(2, RoundingMode.HALF_UP)

    val formatter = NumberFormat.getNumberInstance(locale).apply {
        minimumFractionDigits = 0
        maximumFractionDigits = 2
        isGroupingUsed = true
    }

    return formatter.format(scaled)

}

fun BigDecimal.toFixedDecimals(
    decimals: Int = 2,
    locale: Locale = AppLocaleProvider.current()
): String {
    val scaled = this.setScale(decimals, RoundingMode.HALF_UP)

    val formatter = NumberFormat.getNumberInstance(locale).apply {
        minimumFractionDigits = decimals
        maximumFractionDigits = decimals
        isGroupingUsed = true
    }

    return formatter.format(scaled)
}