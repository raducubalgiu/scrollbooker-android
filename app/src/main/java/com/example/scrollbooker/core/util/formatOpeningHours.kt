package com.example.scrollbooker.core.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.entity.user.userProfile.domain.model.OpeningHours
import kotlin.collections.get

@Composable
fun formatOpeningHours(openingHours: OpeningHours): String? {
    val daysMap = mapOf(
        "Monday" to stringResource(R.string.monday),
        "Tuesday" to stringResource(R.string.tuesday),
        "Wednesday" to stringResource(R.string.wednesday),
        "Thursday" to stringResource(R.string.thursday),
        "Friday" to stringResource(R.string.friday),
        "Saturday" to stringResource(R.string.sunday),
        "Sunday" to stringResource(R.string.sunday),
    )

    fun localizeDay(dayEn: String?): String? {
        return dayEn.let { daysMap[it] }
    }

    return (if(openingHours.openNow) {
        openingHours.closingTime?.let {
            "${stringResource(R.string.isClosingAt)} ${openingHours.closingTime}"
        }
    } else {
        val day = localizeDay(openingHours.nextOpenDay)
        val hour = openingHours.nextOpenTime

        if(day != null && hour != null) {
            "${stringResource(R.string.opens)} ${day.lowercase()} ${stringResource(R.string.at)} $hour"
        } else stringResource(R.string.closed)
    })
}