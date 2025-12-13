package com.example.scrollbooker.ui.myBusiness.myCalendar.components
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.extensions.minutesBetween
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsSlot
import com.example.scrollbooker.ui.theme.bodyMedium
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime

@Composable
fun DayTimeline(
    slots: List<CalendarEventsSlot>,
    dayStart: LocalTime,
    dayEnd: LocalTime,
    slotDurationMinutes: Int,
    gutterWidth: Dp = 56.dp,
    isBlocking: Boolean,
    defaultBlockedLocalDates: Set<LocalDateTime>,
    blockedLocalDates: Set<LocalDateTime>,
    onSlotClick: (CalendarEventsSlot) -> Unit,
) {
    val hourHeight = rememberHourHeight(slotDurationMinutes)

    val totalMinutes = minutesBetween(dayStart, dayEnd).coerceAtLeast(0)
    val dpPerMinute = hourHeight / 60f
    val contentHeight = dpPerMinute * totalMinutes

    val ticks = remember(dayStart, dayEnd, slotDurationMinutes) {
        generateTicks(
            start = dayStart,
            end = dayEnd,
            stepMinutes = slotDurationMinutes
        )
    }

    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(top = 12.dp, bottom = 16.dp)
    ) {
        TimeGutter(
            ticks = ticks,
            hourHeight = hourHeight,
            width = gutterWidth,
            height = contentHeight,
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(contentHeight)
        ) {
            TimeGrid(
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

                val gap = 4.dp

                val height = (dpPerMinute * durationMinutes) - gap
                val visualHeight = height.coerceAtLeast(8.dp)
                val offsetY = (dpPerMinute * startMinute) + gap / 2

                CalendarSlott(
                    height = height,
                    offsetY = offsetY,
                    slot = slot,
                    isBlocked = blockedLocalDates.contains(slot.startDateLocale),
                    isPermanentlyBlocked = defaultBlockedLocalDates.contains(slot.startDateLocale),
                    isBlocking = isBlocking,
                    onSlotClick = onSlotClick
                )
            }
        }
    }
}

@Composable
fun CalendarSlott(
    height: Dp,
    offsetY: Dp,
    slot: CalendarEventsSlot,
    isBlocked: Boolean,
    isPermanentlyBlocked: Boolean,
    isBlocking: Boolean,
    onSlotClick: (CalendarEventsSlot) -> Unit,
    minTouchHeight: Dp = 44.dp
) {
    val visualHeight = height
    val touchHeight = androidx.compose.ui.unit.max(height, minTouchHeight)

    val backgroundColor = when {
        slot.isBooked -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
        isPermanentlyBlocked -> MaterialTheme.colorScheme.error.copy(alpha = 0.18f)
        isBlocked -> MaterialTheme.colorScheme.error.copy(alpha = 0.12f)
        slot.isLastMinute -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.18f)
        else -> MaterialTheme.colorScheme.surface
    }

    val borderColor = when {
        slot.isBooked -> MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
        isPermanentlyBlocked || isBlocked -> MaterialTheme.colorScheme.error.copy(alpha = 0.25f)
        slot.isLastMinute -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.25f)
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    }

    Box(
        modifier = Modifier
            .offset(y = offsetY)
            .height(touchHeight)
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(
                enabled = !slot.isBooked && !isBlocking,
            ) {
                onSlotClick(slot)
            }
    ) {
        Box(
            modifier = Modifier
                .height(visualHeight)
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.TopStart
        ) {
            SlotContent(slot = slot, height = visualHeight)
        }
    }
}

@Composable
private fun TimeGutter(
    ticks: List<LocalTime>,
    hourHeight: Dp,
    width: Dp,
    height: Dp,
) {
    val dpPerMinute = hourHeight / 60f

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
    ) {
        ticks.forEach { t ->
            val minutesFromStart = minutesBetween(ticks.first(), t).coerceAtLeast(0)
            val y = dpPerMinute * minutesFromStart

            Text(
                text = "%02d:%02d".format(t.hour, t.minute),
                style = bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .offset(y = y)
                    .padding(start = 8.dp, top = 2.dp)
            )
        }
    }
}

@Composable
private fun TimeGrid(
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

@Composable
private fun SlotContent(
    slot: CalendarEventsSlot,
    height: Dp
) {
    val isCompact = height < 40.dp
    val isVeryCompact = height < 28.dp

    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = "${slot.startDateLocale!!.toLocalTime()} - ${slot.endDateLocale!!.toLocalTime()}",
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1
        )

        if (!isVeryCompact) {
            when {
                slot.isBooked -> {
                    Text(
                        text = slot.info?.customer?.fullname ?: "Rezervat",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = if (isCompact) 1 else 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                slot.isLastMinute -> {
                    Text(
                        text = "Last minute • ${slot.lastMinuteDiscount}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

                else -> {
                    Text(
                        text = "Disponibil",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

private fun generateTicks(
    start: LocalTime,
    end: LocalTime,
    stepMinutes: Int
): List<LocalTime> {
    require(stepMinutes > 0)

    val result = mutableListOf<LocalTime>()
    var t = start
    while (t <= end) {
        result += t
        t = t.plusMinutes(stepMinutes.toLong())
    }
    return result
}

@Composable
private fun rememberHourHeight(slotDurationMinutes: Int): Dp {
    val minSlotHeight = when (slotDurationMinutes) {
        15 -> 85.dp
        30 -> 100.dp
        45 -> 120.dp
        else -> 150.dp // 60
    }

    val computed = (60f / slotDurationMinutes.toFloat()) * minSlotHeight.value
    return computed.dp.coerceIn(120.dp, 260.dp) // limite ca să nu devină absurd
}














