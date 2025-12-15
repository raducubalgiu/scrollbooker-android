package com.example.scrollbooker.ui.myBusiness.myCalendar.components
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.extensions.minutesBetween
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsSlot
import com.example.scrollbooker.entity.booking.calendar.domain.model.SlotUiStyle
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.slot.CalendarSlot
import com.example.scrollbooker.ui.myBusiness.myCalendar.util.generateTicks
import com.example.scrollbooker.ui.myBusiness.myCalendar.util.rememberHourHeight
import org.threeten.bp.LocalTime

@Composable
fun DayTimeline(
    dayStart: LocalTime,
    dayEnd: LocalTime,
    slots: List<CalendarEventsSlot>,
    slotDuration: Int,
    onStyleResolver: @Composable (CalendarEventsSlot) -> SlotUiStyle,
    onSlotClick: (CalendarEventsSlot) -> Unit,
) {
    val hourHeight = rememberHourHeight(slotDuration)

    val totalMinutes = minutesBetween(dayStart, dayEnd).coerceAtLeast(0)
    val dpPerMinute = hourHeight / 60f
    val contentHeight = dpPerMinute * totalMinutes

    val ticks = remember(dayStart, dayEnd, slotDuration) {
        generateTicks(
            start = dayStart,
            end = dayEnd,
            stepMinutes = slotDuration
        )
    }

    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(top = 12.dp, bottom = 16.dp)
    ) {
        DayTimeGutter(
            ticks = ticks,
            hourHeight = hourHeight,
            height = contentHeight,
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(contentHeight)
        ) {
            DayTimeGrid(
                ticks = ticks,
                dayStart = dayStart,
                hourHeight = hourHeight,
            )

            slots.forEach { slot ->
                val slotStart = slot.startDateLocale?.toLocalTime() ?: return@forEach
                val slotEnd = slot.endDateLocale?.toLocalTime() ?: return@forEach

                val startMinute = kotlin.math.max(
                    0,
                    minutesBetween(dayStart, maxOf(dayStart, slotStart))
                )

                val endMinute = kotlin.math.min(
                    totalMinutes,
                    minutesBetween(dayStart, minOf(dayEnd, slotEnd))
                )

                val durationMinutes = (endMinute - startMinute).coerceAtLeast(0)
                if (durationMinutes == 0) return@forEach

                val gap = 6.dp

                val height = (dpPerMinute * durationMinutes) - gap
                val offsetY = (dpPerMinute * startMinute) + gap / 2

                val style = onStyleResolver(slot)

                CalendarSlot(
                    height = height,
                    offsetY = offsetY,
                    slot = slot,
                    style = style,
                    onSlotClick = onSlotClick
                )
            }
        }
    }
}










