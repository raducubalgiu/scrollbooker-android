package com.example.scrollbooker.core.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R

@Composable
fun translateDayOfWeek(dayEn: String): String? {
    val daysMap = mapOf(
        "Monday" to stringResource(R.string.monday),
        "Tuesday" to stringResource(R.string.tuesday),
        "Wednesday" to stringResource(R.string.wednesday),
        "Thursday" to stringResource(R.string.thursday),
        "Friday" to stringResource(R.string.friday),
        "Saturday" to stringResource(R.string.saturday),
        "Sunday" to stringResource(R.string.sunday),
    )

    return dayEn.let { daysMap[it] }
}