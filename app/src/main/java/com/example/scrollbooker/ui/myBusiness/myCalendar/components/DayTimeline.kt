package com.example.scrollbooker.ui.myBusiness.myCalendar.components
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.extensions.minutesBetween
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEvents
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsSlot
import com.example.scrollbooker.entity.booking.calendar.domain.model.SlotUiStyle
import com.example.scrollbooker.entity.booking.calendar.domain.model.isFreeSlot
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.labelMedium
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime

@Composable
fun DayTimeline(
    slots: List<CalendarEventsSlot>,
    dayStart: LocalTime,
    dayEnd: LocalTime,
    slotDuration: Int,
    onStyleResolver: @Composable (CalendarEventsSlot) -> SlotUiStyle,
    isBlocking: Boolean,
    defaultBlockedLocalDates: Set<LocalDateTime>,
    blockedLocalDates: Set<LocalDateTime>,
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
        TimeGutter(
            ticks = ticks,
            hourHeight = hourHeight,
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

                val gap = 6.dp

                val height = (dpPerMinute * durationMinutes) - gap
                val offsetY = (dpPerMinute * startMinute) + gap / 2

                val style = onStyleResolver(slot)

                CalendarSlot(
                    height = height,
                    offsetY = offsetY,
                    slot = slot,
                    style = style,
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
private fun TimeGutter(
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
fun CalendarSlot(
    height: Dp,
    offsetY: Dp,
    slot: CalendarEventsSlot,
    style: SlotUiStyle,
    isBlocked: Boolean,
    isPermanentlyBlocked: Boolean,
    isBlocking: Boolean,
    onSlotClick: (CalendarEventsSlot) -> Unit,
    minTouchHeight: Dp = 44.dp
) {
    val visualHeight = height
    val touchHeight = androidx.compose.ui.unit.max(height, minTouchHeight)

    Box(
        modifier = Modifier
            .offset(y = offsetY)
            .height(touchHeight)
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clip(shape = ShapeDefaults.Medium)
            .background(style.background)
            .border(
                width = 1.dp,
                color = style.borderColor,
                shape = ShapeDefaults.Medium
            )
            .clickable(enabled = !slot.isBooked && !isBlocking) {
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
            SlotContent(
                slot = slot,
                height = visualHeight,
                lineColor = style.lineColor
            )
        }
    }
}

@Composable
private fun SlotContent(
    slot: CalendarEventsSlot,
    lineColor: Color,
    height: Dp
) {
    val isCompact = height < 40.dp
    val isVeryCompact = height < 28.dp
    val now = LocalDateTime.now()
    val isBefore = slot.startDateLocale?.isBefore(now) != false

    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(Modifier.fillMaxWidth()) {
            if(!slot.isFreeSlot()) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight()
                        .clip(shape = ShapeDefaults.ExtraLarge)
                        .background(lineColor)
                )

                Spacer(Modifier.width(SpacingS))
            }

            Column {
                Text(
                    text = "${slot.startDateLocale!!.toLocalTime()} - ${slot.endDateLocale!!.toLocalTime()}",
                    style = labelMedium,
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

                        slot.isBlocked -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    color = lineColor,
                                    text = slot.info?.message ?: "Blocat",
                                )
                            }
                        }

                        isBefore -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    color = Divider,
                                    text = "Slot Vacant"
                                )
                            }
                        }

                        else -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    modifier = Modifier.size(40.dp),
                                    painter = painterResource(R.drawable.ic_circle_plus_outline),
                                    contentDescription = null,
                                    tint = Divider
                                )
                            }
                        }
                    }
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
        15 -> 130.dp
        30 -> 140.dp
        45 -> 150.dp
        else -> 170.dp
    }

    val computed = (60f / slotDurationMinutes.toFloat()) * minSlotHeight.value
    return computed.dp.coerceIn(120.dp, 260.dp)
}


@Composable
fun SlotCheckbox(
    isBlocked: Boolean,
    isEnabled: Boolean,
    onCheckedChange: (() -> Unit)? = null
) {
    Box(Modifier.fillMaxSize()) {
        Checkbox(
            modifier = Modifier.align(Alignment.TopEnd),
            checked = isBlocked,
            enabled = isEnabled,
            onCheckedChange = { onCheckedChange?.invoke() },
            colors = CheckboxColors(
                checkedCheckmarkColor = Color.White,
                uncheckedCheckmarkColor = Color.Transparent,
                checkedBoxColor = Primary,
                uncheckedBoxColor = Color.Transparent,
                disabledCheckedBoxColor = Divider,
                disabledUncheckedBoxColor = Divider,
                disabledIndeterminateBoxColor = Divider,
                checkedBorderColor = Primary,
                uncheckedBorderColor = Color.Gray,
                disabledBorderColor = Divider,
                disabledUncheckedBorderColor = Divider,
                disabledIndeterminateBorderColor = Divider
            )
        )
    }
}











