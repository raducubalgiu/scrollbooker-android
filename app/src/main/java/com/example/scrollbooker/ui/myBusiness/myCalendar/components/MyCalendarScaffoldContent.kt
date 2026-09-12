package com.example.scrollbooker.ui.myBusiness.myCalendar.components
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.extensions.displayDatePeriod
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.availability.domain.model.CalendarEvents
import com.example.scrollbooker.entity.booking.availability.domain.model.hasDayFreeSlots
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.ui.myBusiness.myCalendar.BlockUiState
import com.example.scrollbooker.ui.myBusiness.myCalendar.MyCalendarAction
import com.example.scrollbooker.ui.myBusiness.myCalendar.MyCalendarAction.*
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarHeader
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarHeaderState
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarHeaderStateAction
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarHeaderStateAction.HandleNextWeek
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarHeaderStateAction.HandlePreviousWeek
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarHeaderStateAction.OnChangeTab
import com.example.scrollbooker.components.customized.calendar.CalendarHeaderState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.threeten.bp.LocalTime

@Composable
fun MyCalendarScaffoldContent(
    headerState: FeatureState<CalendarHeaderState>,
    calendarEvents: FeatureState<CalendarEvents>,
    daySchedule: Schedule?,
    slotDuration: Int,
    businessDayWindow: Pair<LocalTime, LocalTime>?,
    blockUiState: BlockUiState,
    isRefreshing: Boolean,
    hasEmployees: Boolean,
    selectedEmployee: Employee?,
    onAction: (MyCalendarAction) -> Unit
) {
    val scope = rememberCoroutineScope()
    val hasFreeSlots = (calendarEvents as? FeatureState.Success)?.data?.hasDayFreeSlots() == true

    // Keeps the last successfully loaded header (week/day tabs, available days) mounted across a
    // context change (e.g. switching employee) instead of tearing the whole screen down to a
    // blank loading state - that used to also reset the week/day pager position, since the
    // composable subtree (and its rememberPagerState) was fully removed while headerState was
    // Loading. Only the slots below (MyCalendarPagerSection) actually need to reload per employee.
    var cachedHeader by remember { mutableStateOf<CalendarHeaderState?>(null) }
    LaunchedEffect(headerState) {
        if (headerState is FeatureState.Success) cachedHeader = headerState.data
    }
    val isHeaderRefreshing = headerState is FeatureState.Loading

    val calendar = cachedHeader

    when {
        calendar == null && headerState is FeatureState.Error -> ErrorScreen()
        calendar == null -> LoadingScreen()
        else -> {
            val config = calendar.config
            val calendarDays = calendar.calendarDays
            val availableDays = calendar.calendarAvailableDays

            if(calendarDays.isEmpty()) {
                ErrorScreen()
                return
            }

            val weekPagerState = rememberPagerState(initialPage = config.initialWeekPage) { config.totalWeeks }
            val dayPagerState = rememberPagerState(initialPage = config.initialDayPage) { 7 }

            val currentDayIndex = dayPagerState.currentPage
            val currentWeekIndex = weekPagerState.currentPage

            val latestOnAction = rememberUpdatedState(onAction)

            LaunchedEffect(currentWeekIndex, currentDayIndex) {
                val dayIndex = currentWeekIndex * 7 + currentDayIndex
                val day = calendarDays.getOrNull(dayIndex)
                if(day != null) {
                    latestOnAction.value(DayChanged(day))
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

            Column(modifier = Modifier.fillMaxSize()) {
                MyCalendarHeader(
                    state = MyCalendarHeaderState(
                        weekPagerState = weekPagerState,
                        selectedTabIndex = dayPagerState.currentPage,
                        period = period,
                        enableBack = enableBack,
                        enableNext = enableNext,
                        availableDays = availableDays,
                        calendarDays = calendarDays,
                        isBlocking = blockUiState.isBlocking,
                        hasFreeSlots = hasFreeSlots,
                        hasEmployees = hasEmployees,
                        selectedEmployeeName = selectedEmployee?.fullName,
                        selectedEmployeeAvatar = selectedEmployee?.avatar,
                        isRefreshing = isHeaderRefreshing
                    ),
                    onAction = { action ->
                        handleHeaderAction(
                            action = action,
                            onAction = onAction,
                            handleNextWeek = { handleNextWeek() },
                            handlePreviousWeek = { handlePreviousWeek() },
                            dayPagerState = dayPagerState,
                            scope = scope
                        )
                    }
                )

                MyCalendarPagerSection(
                    dayPagerState = dayPagerState,
                    calendarEvents = calendarEvents,
                    daySchedule = daySchedule,
                    slotDuration = slotDuration,
                    businessDayWindow = businessDayWindow,
                    blockUiState = blockUiState,
                    isRefreshing = isRefreshing,
                    onSlotClick = { onAction(SlotClick(it)) },
                    onDayRefresh = { onAction(DayRefresh) }
                )
            }
        }
    }
}

private fun handleHeaderAction(
    action: MyCalendarHeaderStateAction,
    onAction: (MyCalendarAction) -> Unit,
    handleNextWeek: () -> Unit,
    handlePreviousWeek: () -> Unit,
    dayPagerState: PagerState,
    scope: CoroutineScope
) {
    when(action) {
        is MyCalendarHeaderStateAction.Back -> onAction(Back)
        is MyCalendarHeaderStateAction.Settings -> onAction(Settings)
        is MyCalendarHeaderStateAction.OnBlockToggle -> onAction(OnBlockToggle)
        is HandleNextWeek -> handleNextWeek()
        is HandlePreviousWeek -> handlePreviousWeek()
        is MyCalendarHeaderStateAction.OpenEmployeeSheet -> onAction(OpenEmployeeSheet)
        is OnChangeTab -> {
            onAction(DayChanged(action.date))

            scope.launch {
                dayPagerState.animateScrollToPage(action.index)
            }
        }
    }
}