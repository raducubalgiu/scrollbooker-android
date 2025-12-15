package com.example.scrollbooker.ui.myBusiness.myCalendar.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun rememberHourHeight(slotDurationMinutes: Int): Dp {
    val minSlotHeight = when (slotDurationMinutes) {
        15 -> 130.dp
        30 -> 140.dp
        45 -> 150.dp
        else -> 170.dp
    }

    val computed = (60f / slotDurationMinutes.toFloat()) * minSlotHeight.value
    return computed.dp.coerceIn(120.dp, 260.dp)
}
