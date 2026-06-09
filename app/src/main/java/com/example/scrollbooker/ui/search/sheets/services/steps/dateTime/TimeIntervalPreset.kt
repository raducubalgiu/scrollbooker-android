package com.example.scrollbooker.ui.search.sheets.services.steps.dateTime

import androidx.annotation.StringRes
import com.example.scrollbooker.R
import org.threeten.bp.LocalTime

enum class TimeIntervalPreset(
    val start: LocalTime?,
    val end: LocalTime?,
    @StringRes val labelRes: Int,
    val description: String? = null
) {
    ANYTIME(
        start = null,
        end = null,
        labelRes = R.string.anytime
    ),
    MORNING(
        start = LocalTime.of(9, 0),
        end = LocalTime.of(12, 0),
        labelRes = R.string.morning,
        description = "09:00 - 12:00"
    ),
    LUNCH(
        start = LocalTime.of(12, 0),
        end = LocalTime.of(18, 0),
        labelRes = R.string.afternoon,
        description = "12:00 - 18:00"
    ),
    EVENING(
        start = LocalTime.of(18, 0),
        end = LocalTime.of(22, 0),
        labelRes = R.string.evening,
        description = "18:00 - 22:00"
    ),
    CUSTOM(
        start = null,
        end = null,
        labelRes = R.string.custom
    );

    val hasTime: Boolean get() = description != null
}