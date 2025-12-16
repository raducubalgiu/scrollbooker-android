package com.example.scrollbooker.ui.myBusiness.myCalendar.components
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.extensions.displayDatePeriod
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEvents
import com.example.scrollbooker.entity.booking.calendar.domain.model.hasDayFreeSlots
import com.example.scrollbooker.ui.myBusiness.myCalendar.BlockUiState
import com.example.scrollbooker.ui.myBusiness.myCalendar.MyCalendarAction
import com.example.scrollbooker.ui.myBusiness.myCalendar.MyCalendarAction.*
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarHeaderState
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarHeaderStateAction
import com.example.scrollbooker.ui.shared.calendar.CalendarHeaderState
import kotlinx.coroutines.launch

@Composable
fun MyCalendarScaffoldContent(
    headerState: FeatureState<CalendarHeaderState>,
    calendarEvents: FeatureState<CalendarEvents>,
    slotDuration: Int,
    blockUiState: BlockUiState,
    onAction: (MyCalendarAction) -> Unit
) {
    val scope = rememberCoroutineScope()
    val hasFreeSlots = (calendarEvents as? FeatureState.Success)?.data?.hasDayFreeSlots() == true

    when(val header = headerState) {
        is FeatureState.Error -> ErrorScreen()
        is FeatureState.Loading -> LoadingScreen()
        is FeatureState.Success -> {
            val calendar = header.data
            val config = calendar.config
            val calendarDays = calendar.calendarDays
            val availableDays = calendar.calendarAvailableDays

            if(calendar.calendarDays.isEmpty()) {
                ErrorScreen()
                return
            }

            val weekPagerState = rememberPagerState(initialPage = config.initialWeekPage) { config.totalWeeks }
            val dayPagerState = rememberPagerState(initialPage = config.initialDayPage) { 7 }

            val currentDayIndex = dayPagerState.currentPage
            val currentWeekIndex = weekPagerState.currentPage

            LaunchedEffect(currentWeekIndex, currentDayIndex) {
                val dayIndex = currentWeekIndex * 7 + currentDayIndex
                val day = calendarDays.getOrNull(dayIndex)
                if(day != null) { onAction(DayChanged(day)) }
            }

            val currentWeekDates = calendarDays.drop(currentWeekIndex * 7).take(7)
            val period = displayDatePeriod(currentWeekDates.first(), currentWeekDates.last())

            val enableBack = currentWeekIndex > 0
            val enableNext = currentWeekIndex < config.totalWeeks - 1

            fun handlePreviousWeek() {
                scope.launch { weekPagerState.animateScrollToPage(currentWeekIndex - 1) }
            }

            fun handleNextWeek() {
                scope.launch { weekPagerState.animateScrollToPage(currentWeekIndex + 1) }
            }

            Column(modifier = Modifier.fillMaxSize()) {
                MyCalendarHeader(
                    state = MyCalendarHeaderState(
                        weekPagerState = weekPagerState,
                        selectedTabIndex = dayPagerState.currentPage,
                        period = period,
                        slotDuration = slotDuration,
                        enableBack = enableBack,
                        enableNext = enableNext,
                        availableDays = availableDays,
                        calendarDays = calendarDays,
                        isBlocking = blockUiState.isBlocking,
                        hasFreeSlots = hasFreeSlots
                    ),
                    onAction = { action ->
                        when(action) {
                            is MyCalendarHeaderStateAction.Back -> onAction(Back)
                            is MyCalendarHeaderStateAction.Settings -> {}
                            is MyCalendarHeaderStateAction.OnBlockToggle -> onAction(OnBlockToggle)
                            is MyCalendarHeaderStateAction.HandleNextWeek -> handleNextWeek()
                            is MyCalendarHeaderStateAction.HandlePreviousWeek -> handlePreviousWeek()
                            is MyCalendarHeaderStateAction.OnSlotChange -> onAction(SlotDurationChanged(action.slotDuration))
                            is MyCalendarHeaderStateAction.OnChangeTab -> {
                                scope.launch {
                                    onAction(DayChanged(action.date))
                                    dayPagerState.animateScrollToPage(action.index)
                                }
                            }
                        }
                    }
                )

                MyCalendarPagerSection(
                    dayPagerState = dayPagerState,
                    calendarEvents = calendarEvents,
                    slotDuration = slotDuration,
                    blockUiState = blockUiState,
                    onSlotClick = { onAction(SlotClick(it)) },
                    onDayRefresh = { onAction(DayRefresh) }
                )
            }
        }
    }
}