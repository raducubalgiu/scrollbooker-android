package com.example.scrollbooker.ui.myBusiness.myCalendar.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.layout.MessageScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEvents
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsSlot
import org.threeten.bp.LocalDateTime

@Composable
fun MyCalendarPagerSection(
    dayPagerState: PagerState,
    calendarEvents: FeatureState<CalendarEvents>,
    slotDuration: Int,
    isBlocking: Boolean,
    defaultBlockedLocalDates: Set<LocalDateTime>,
    blockedLocalDates: Set<LocalDateTime>,
    onIsBlocking: (Boolean) -> Unit,
    onSlotClick: (CalendarEventsSlot) -> Unit
) {
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
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Success -> {
                    val calendarEvents = events.data

                    val dayStart = calendarEvents.minSlotTime?.toLocalTime()
                    val dayEnd = calendarEvents.maxSlotTime?.toLocalTime()
                    val slots = events.data.days.first().slots

                    if(dayStart != null && dayEnd != null) {
                        DayTimeline(
                            dayStart = dayStart,
                            dayEnd = dayEnd,
                            slots = slots,
                            slotDuration = slotDuration,
                            isBlocking = isBlocking,
                            onStyleResolver = { slot -> with(calendarEvents) { slot.resolveUiStyle() } },
                            defaultBlockedLocalDates = defaultBlockedLocalDates,
                            blockedLocalDates = blockedLocalDates,
                            onIsBlocking = onIsBlocking,
                            onSlotClick = onSlotClick
                        )
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