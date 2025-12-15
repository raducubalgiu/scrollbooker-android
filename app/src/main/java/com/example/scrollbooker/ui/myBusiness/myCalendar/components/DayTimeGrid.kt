package com.example.scrollbooker.ui.myBusiness.myCalendar.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.extensions.minutesBetween
import org.threeten.bp.LocalTime
import kotlin.collections.forEach

@Composable
fun DayTimeGrid(
    ticks: List<LocalTime>,
    dayStart: LocalTime,
    hourHeight: Dp,
) {
    val totalMinutes = minutesBetween(dayStart, ticks.last()).coerceAtLeast(0)
    val dpPerMinute = hourHeight / 60f
    val totalHeight = dpPerMinute * totalMinutes

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(totalHeight)
    ) {
        val dpPerMinutePx = dpPerMinute.toPx()

        ticks.forEach { t ->
            val m = minutesBetween(dayStart, t).coerceAtLeast(0)
            val y = m * dpPerMinutePx

            val isHour = (t.minute == 0)

            drawLine(
                color = Color.Black.copy(alpha = if (isHour) 0.08f else 0.05f),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = (if (isHour) 1.dp else 1.dp).toPx()
            )
        }
    }
}