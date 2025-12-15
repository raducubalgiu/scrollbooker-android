package com.example.scrollbooker.ui.myBusiness.myCalendar.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.extensions.minutesBetween
import com.example.scrollbooker.ui.theme.bodyMedium
import org.threeten.bp.LocalTime
import kotlin.collections.forEach

@Composable
fun DayTimeGutter(
    ticks: List<LocalTime>,
    hourHeight: Dp,
    height: Dp,
) {
    val dpPerMinute = hourHeight / 60f

    Box(
        modifier = Modifier
            .width(56.dp)
            .height(height)
    ) {
        ticks.forEach { t ->
            val minutesFromStart = minutesBetween(ticks.first(), t).coerceAtLeast(0)
            val y = dpPerMinute * minutesFromStart

            Text(
                text = "%02d:%02d".format(t.hour, t.minute),
                style = bodyMedium,
                modifier = Modifier
                    .offset(y = y)
                    .padding(start = 8.dp, top = 2.dp)
            )
        }
    }
}