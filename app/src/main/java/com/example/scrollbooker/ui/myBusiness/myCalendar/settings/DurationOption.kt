package com.example.scrollbooker.ui.myBusiness.myCalendar.settings

import androidx.annotation.StringRes
import com.example.scrollbooker.R

data class DurationOption(val minutes: Int, @StringRes val labelRes: Int)

val SLOT_DURATION_OPTIONS = listOf(
    DurationOption(30, R.string.minutes30),
    DurationOption(45, R.string.minutes45),
    DurationOption(60, R.string.hour1),
    DurationOption(90, R.string.hour1Minutes30),
)

val GAP_OPTIONS = listOf(
    DurationOption(0, R.string.noGap),
    DurationOption(5, R.string.minutes5),
    DurationOption(10, R.string.minutes10),
    DurationOption(15, R.string.minutes15),
)

@StringRes
fun List<DurationOption>.labelResFor(minutes: Int): Int? =
    firstOrNull { it.minutes == minutes }?.labelRes
