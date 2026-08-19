package com.example.scrollbooker.ui.myBusiness.myDashboard

import androidx.annotation.StringRes
import com.example.scrollbooker.R

enum class DashboardPeriod(val label: String, @StringRes val titleRes: Int) {
    SEVEN_DAYS("7 days", R.string.period_seven_days),
    ONE_MONTH("1 month", R.string.period_one_month),
    THREE_MONTHS("3 months", R.string.period_three_months),
    SIX_MONTHS("6 months", R.string.period_six_months),
    ONE_YEAR("1 year", R.string.period_one_year)
}
