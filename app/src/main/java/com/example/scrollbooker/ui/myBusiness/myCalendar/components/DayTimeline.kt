package com.example.scrollbooker.ui.myBusiness.myCalendar.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.minutesBetween
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsSlot
import com.example.scrollbooker.entity.booking.calendar.domain.model.timeFromLocale
import com.example.scrollbooker.ui.theme.bodyLarge
import org.threeten.bp.LocalTime
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

@Composable
fun DayTimeline(
    slots: List<CalendarEventsSlot>,
    dayStart: LocalTime,
    dayEnd: LocalTime,
    slotDuration: Int,
    slotHeight: Dp = 150.dp,
    gutterWidth: Dp = 56.dp,
    onSlotClick: (CalendarEventsSlot) -> Unit = {}
) {
    val ticks = remember(dayStart, dayEnd, slotDuration) {
        generateTicks(dayStart, dayEnd, slotDuration)
    }

    // cat inseamna 1 min pe axa Y
    val dpPerMinute = slotHeight / slotDuration.toFloat()
    val totalMinutes = minutesBetween(dayStart, dayEnd)
    val totalHeightDp = dpPerMinute * totalMinutes

    Row(modifier = Modifier
        .fillMaxWidth()
        .heightIn(min = 200.dp)
        .verticalScroll(rememberScrollState())
        .padding(top = SpacingM, bottom = BasePadding)
    ) {
        Column(modifier = Modifier
            .width(gutterWidth)
            .height(totalHeightDp),
            verticalArrangement = Arrangement.Top
        ) {
            ticks.forEach { t ->
                Box(modifier = Modifier
                    .height(slotHeight)
                    .fillMaxWidth(),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        text = t.toString(),
                        style = bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 2.dp, start = 8.dp)
                    )
                }
            }
        }

        Box(modifier = Modifier
            .weight(1f)
            .height(totalHeightDp)
        ) {
            slots.forEach { slot ->
                val bounds = slot.timeFromLocale()

                val s = max(0, minutesBetween(dayStart, maxOf(dayStart, bounds.startTime)))
                val e = min(totalMinutes, minutesBetween(dayStart, minOf(dayEnd, bounds.endTime)))
                val duration = max(0, e - s)

                if(duration > 0) {
                    val offsetY = dpPerMinute * s
                    val height = dpPerMinute * duration

                    CalendarSlot(
                        height = height,
                        offsetY = offsetY,
                        slot = slot,
                        onSlotClick = onSlotClick
                    )
                }
            }
        }
    }
}

private fun generateTicks(
    start: LocalTime,
    end: LocalTime,
    slotDuration: Int
): List<LocalTime> {
    val totalMin = max(0, minutesBetween(start, end))
    val steps = ceil(totalMin / slotDuration.toFloat()).toInt() + 1
    return (0 until steps).map { start.plusMinutes((it * slotDuration).toLong()) }
}