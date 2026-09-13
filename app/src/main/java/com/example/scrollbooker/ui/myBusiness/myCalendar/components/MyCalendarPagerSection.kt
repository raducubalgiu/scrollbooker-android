package com.example.scrollbooker.ui.myBusiness.myCalendar.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.MessageScreen
import com.example.scrollbooker.components.customized.Refresh
import com.example.scrollbooker.core.extensions.minutesBetween
import com.example.scrollbooker.core.extensions.parseTimeStringToLocalTime
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.availability.domain.model.BlockStatus
import com.example.scrollbooker.entity.booking.availability.domain.model.CalendarEvents
import com.example.scrollbooker.entity.booking.availability.domain.model.CalendarEventsSlot
import com.example.scrollbooker.entity.booking.availability.domain.model.blockStatus
import com.example.scrollbooker.entity.booking.availability.domain.model.isFreeSlot
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.ui.myBusiness.myCalendar.BlockUiState
import com.example.scrollbooker.ui.myBusiness.myCalendar.util.rememberHourHeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCalendarPagerSection(
    dayPagerState: PagerState,
    calendarEvents: FeatureState<CalendarEvents>,
    daySchedule: Schedule?,
    slotDuration: Int,
    blockUiState: BlockUiState,
    isRefreshing: Boolean,
    onSlotClick: (CalendarEventsSlot) -> Unit,
    onDayRefresh: () -> Unit
) {
    val scheduleStart = remember(daySchedule) { parseTimeStringToLocalTime(daySchedule?.startTime) }
    val scheduleEnd = remember(daySchedule) { parseTimeStringToLocalTime(daySchedule?.endTime) }

    HorizontalPager(
        state = dayPagerState,
        pageSize = PageSize.Fill,
        beyondViewportPageCount = 0,
        modifier = Modifier.fillMaxSize()
    ) { index ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = BasePadding),
            contentAlignment = Alignment.TopStart
        ) {
            when(val events = calendarEvents) {
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Loading -> DayTimelineShimmer(slotDuration = slotDuration)
                is FeatureState.Success -> {
                    val calendarEvents = events.data
                    val slots = calendarEvents.days.firstOrNull()?.slots ?: emptyList()

                    val slotsStart = remember(slots) { slots.mapNotNull { it.startDateLocale?.toLocalTime() }.minOrNull() }
                    val slotsEnd = remember(slots) { slots.mapNotNull { it.endDateLocale?.toLocalTime() }.maxOrNull() }

                    val dayStart = listOfNotNull(scheduleStart, slotsStart).minOrNull()
                    val dayEnd = listOfNotNull(scheduleEnd, slotsEnd).maxOrNull()

                    if(dayStart != null && dayEnd != null) {
                        val scrollState = rememberScrollState()
                        val hourHeight = rememberHourHeight(slotDuration)
                        val density = LocalDensity.current

                        val nextEligibleSlot = remember(slots, blockUiState.defaultBlockedLocalDates, blockUiState.blockedLocalDates) {
                            slots.sortedBy { it.startDateLocale }
                                .firstOrNull {
                                    it.isFreeSlot() &&
                                        it.blockStatus(blockUiState.defaultBlockedLocalDates, blockUiState.blockedLocalDates) == BlockStatus.None
                                }
                        }

                        LaunchedEffect(blockUiState.isBlocking) {
                            if (!blockUiState.isBlocking) return@LaunchedEffect
                            val targetStart = nextEligibleSlot?.startDateLocale?.toLocalTime() ?: return@LaunchedEffect

                            val totalMinutes = minutesBetween(dayStart, dayEnd).coerceAtLeast(0)
                            val startMinute = minutesBetween(dayStart, maxOf(dayStart, targetStart)).coerceIn(0, totalMinutes)
                            val dpPerMinute = hourHeight / 60f
                            // A little breathing room above the target slot, not flush to the top edge.
                            val offsetDp = (dpPerMinute * startMinute) - 16.dp

                            val offsetPx = with(density) { offsetDp.coerceAtLeast(0.dp).roundToPx() }
                            scrollState.animateScrollTo(offsetPx)
                        }

                        Refresh(
                            isRefreshing = isRefreshing,
                            onRefresh = onDayRefresh,
                        ) {
                            if (isRefreshing) {
                                DayTimelineShimmer(slotDuration = slotDuration)
                            } else {
                                DayTimeline(
                                    dayStart = dayStart,
                                    dayEnd = dayEnd,
                                    slots = slots,
                                    slotDuration = slotDuration,
                                    blockUiState = blockUiState,
                                    scrollState = scrollState,
                                    onStyleResolver = { slot -> with(calendarEvents) { slot.resolveUiStyle() } },
                                    onSlotClick = onSlotClick
                                )
                            }
                        }
                    } else {
                        MessageScreen(
                            modifier = Modifier.padding(top = 100.dp),
                            arrangement = Arrangement.Top,
                            icon = painterResource(R.drawable.ic_calendar_outline_stroke_small),
                            message = stringResource(R.string.notFoundBookings),
                        )
                    }
                }
            }
        }
    }
}