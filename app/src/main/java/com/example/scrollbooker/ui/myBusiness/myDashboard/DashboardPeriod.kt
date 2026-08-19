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
}
enum class DashboardPeriod(val label: String, @StringRes val titleRes: Int) {
    SEVEN_DAYS("7 days", R.string.period_seven_days),
    ONE_MONTH("1 month", R.string.period_one_month),
    THREE_MONTHS("3 months", R.string.period_three_months),
    SIX_MONTHS("6 months", R.string.period_six_months),
    ONE_YEAR("1 year", R.string.period_one_year);

    fun getDateRange(referenceDate: LocalDate = LocalDate.now()): DashboardDateRange {
        val startDate = referenceDate
        val endDate = when (this) {
            SEVEN_DAYS -> referenceDate.plusDays(7)
            ONE_MONTH -> referenceDate.plusMonths(1)
            THREE_MONTHS -> referenceDate.plusMonths(3)
            SIX_MONTHS -> referenceDate.plusMonths(6)
            ONE_YEAR -> referenceDate.plusYears(1)
        }
        return DashboardDateRange(startDate, endDate)
    }
}
