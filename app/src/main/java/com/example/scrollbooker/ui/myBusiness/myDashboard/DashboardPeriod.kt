package com.example.scrollbooker.ui.myBusiness.myDashboard

import androidx.annotation.StringRes
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.AppLocaleProvider
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

data class DashboardDateRange(
    val startDate: LocalDate,
    val endDate: LocalDate
) {
    fun format(
        pattern: String = "dd MMM",
        locale: Locale = AppLocaleProvider.current()
    ): String {
        val formatter = DateTimeFormatter.ofPattern(pattern, locale)
        return "${startDate.format(formatter)} - ${endDate.format(formatter)}"
    }

    fun toApiStartDate(): String = startDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
    fun toApiEndDate(): String = endDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
}

enum class DashboardPeriod(val label: String, @StringRes val titleRes: Int) {
    SEVEN_DAYS("7 days", R.string.period_seven_days),
    ONE_MONTH("1 month", R.string.period_one_month),
    THREE_MONTHS("3 months", R.string.period_three_months),
    SIX_MONTHS("6 months", R.string.period_six_months),
    ONE_YEAR("1 year", R.string.period_one_year);

    fun getDateRange(referenceDate: LocalDate = LocalDate.now()): DashboardDateRange {
        val endDate = referenceDate
        val startDate = when (this) {
            SEVEN_DAYS -> referenceDate.minusDays(7)
            ONE_MONTH -> referenceDate.minusMonths(1)
            THREE_MONTHS -> referenceDate.minusMonths(3)
            SIX_MONTHS -> referenceDate.minusMonths(6)
            ONE_YEAR -> referenceDate.minusYears(1)
        }
        return DashboardDateRange(startDate, endDate)
    }
}
