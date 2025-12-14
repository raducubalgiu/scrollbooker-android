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
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsSlot
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarHeaderState
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarHeaderStateAction
import com.example.scrollbooker.ui.shared.calendar.CalendarHeaderState
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

@Composable
fun MyCalendarScaffoldContent(
    headerState: FeatureState<CalendarHeaderState>,
    calendarEvents: FeatureState<CalendarEvents>,
    slotDuration: Int,
    isBlocking: Boolean,
    onDayChange: (LocalDate) -> Unit,
    onSlotDurationChange: (String?) -> Unit,
    onOpenOwnClientSheet: (CalendarEventsSlot) -> Unit,
    defaultBlockedLocalDates: Set<LocalDateTime>,
    blockedLocalDates: Set<LocalDateTime>,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()

    when(headerState) {
        is FeatureState.Error -> ErrorScreen()
        is FeatureState.Loading -> LoadingScreen()
        is FeatureState.Success -> {
            val calendar = headerState.data
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
                if(day != null) {
                    onDayChange(day)
                }
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

            val myCalendarUIState = MyCalendarHeaderState(
                weekPagerState = weekPagerState,
                selectedTabIndex = dayPagerState.currentPage,
                period = period,
                slotDuration = slotDuration,
                isBlocking = isBlocking,
                enableBack = enableBack,
                enableNext = enableNext,
                availableDays = availableDays,
                calendarDays = calendarDays
            )

            Column(modifier = Modifier.fillMaxSize()) {
                MyCalendarHeaderSection(
                    state = myCalendarUIState,
                    period = period,
                    onBack = onBack,
                    onAction = { action ->
                        when(action) {
                            is MyCalendarHeaderStateAction.HandleNextWeek -> handleNextWeek()
                            is MyCalendarHeaderStateAction.HandlePreviousWeek -> handlePreviousWeek()
                            is MyCalendarHeaderStateAction.OnChangeTab -> {
                                scope.launch {
                                    onDayChange(action.date)
                                    dayPagerState.animateScrollToPage(action.index)
                                }
                            }
                            is MyCalendarHeaderStateAction.OnIsBlocking -> {
//                                if(!isBlocking) isBlocking = true
//                                else {
//                                    viewModel.resetSelectedLocalDates()
//                                    isBlocking = false
//                                }
                            }
                            is MyCalendarHeaderStateAction.OnSlotChange -> {
                                onSlotDurationChange(action.slotDuration)
                            }
                        }
                    }
                )

                MyCalendarPagerSection(
                    dayPagerState = dayPagerState,
                    calendarEvents = calendarEvents,
                    slotDuration = slotDuration,
                    isBlocking = isBlocking,
                    defaultBlockedLocalDates = defaultBlockedLocalDates,
                    blockedLocalDates = blockedLocalDates,
                    onSlotClick = { slot ->
                        when {
                            slot.isBooked -> {
                                // Should Redirect to DetailScreen
                            }
                            isBlocking -> {
                                slot.startDateLocale?.let {
                                    //viewModel.setBlockDate(slot.startDateLocale)
                                }
                            }
                            else -> {
                                onOpenOwnClientSheet(slot)
                            }
                        }
                    }
                )
            }
        }
    }
}