package com.example.scrollbooker.ui.sharedModules.calendar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.displayDatePeriod
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.util.Locale
import com.example.scrollbooker.core.util.displayShortDayOfWeek
import com.example.scrollbooker.entity.booking.calendar.domain.model.AvailableDay
import com.example.scrollbooker.entity.booking.calendar.domain.model.Slot
import com.example.scrollbooker.ui.sharedModules.calendar.components.CalendarDayTab
import com.example.scrollbooker.ui.sharedModules.calendar.components.CalendarHeader
import com.example.scrollbooker.ui.sharedModules.calendar.components.slots.SlotsList

@Composable
fun Calendar(
    availableDayTimeslots: FeatureState<AvailableDay>,
    calendarDays: List<LocalDate>,
    availableDays: FeatureState<List<LocalDate>>,
    config: CalendarConfig?,
    onDayChange: (LocalDate) -> Unit,
    onSelectSlot: (Slot) -> Unit
) {
    if(calendarDays.isEmpty() || config == null) return

    val locale = Locale("ro")
    val coroutineScope = rememberCoroutineScope()
    val today = LocalDate.now()

    val weekPagerState = rememberPagerState(initialPage = config.initialWeekPage) { config.totalWeeks }
    val dayPagerState = rememberPagerState(initialPage = config.initialDayPage) { 7 }

    val currentDayIndex = dayPagerState.currentPage
    val currentWeekIndex = weekPagerState.currentPage

    val currentWeekDates = calendarDays.drop(currentWeekIndex * 7).take(7)

    val selectedDay = calendarDays.drop(currentWeekIndex * 7).getOrNull(currentDayIndex)

    val enableBack = currentWeekIndex > 0 && selectedDay?.isAfter(today.minusDays(1)) == true
    val enableNext = currentWeekIndex < config.totalWeeks - 1

    fun handlePreviousWeek() {
        coroutineScope.launch {
            weekPagerState.animateScrollToPage(currentWeekIndex - 1)
        }
    }

    fun handleNextWeek() {
        coroutineScope.launch {
            weekPagerState.animateScrollToPage(currentWeekIndex + 1)
        }
    }

    LaunchedEffect(currentWeekIndex) {
        if(dayPagerState.currentPage != 0) {
            dayPagerState.scrollToPage(0)
        }
    }

    LaunchedEffect(currentWeekIndex, currentDayIndex) {
        selectedDay?.let {
            onDayChange(it)
        }
    }

    Column(modifier = Modifier.statusBarsPadding()) {
        CalendarHeader(
            period = displayDatePeriod(currentWeekDates.first(), currentWeekDates.last(), locale),
            enableBack = enableBack,
            enableNext = enableNext,
            handlePreviousWeek = { handlePreviousWeek() },
            handleNextWeek = { handleNextWeek() }
        )

        HorizontalPager(
            state = weekPagerState,
            pageSize = PageSize.Fill,
            modifier = Modifier
                .fillMaxWidth()
                .height(95.dp)
                .padding(horizontal = BasePadding),
            beyondViewportPageCount = 0
        ) { page ->
            val weekDates = calendarDays.drop(page * 7).take(7)

            TabRow(
                selectedTabIndex = dayPagerState.currentPage,
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.Transparent,
                contentColor = OnBackground,
                indicator = {},
            ) {
                weekDates.forEachIndexed { index, date ->
                    val isAvailable = (availableDays as? FeatureState.Success)
                        ?.data
                        ?.contains(date) == true

                    CalendarDayTab(
                        date = date,
                        isCurrentTab = index == dayPagerState.currentPage,
                        onChangeTab = {
                            coroutineScope.launch {
                                onDayChange(date)
                                dayPagerState.animateScrollToPage(index)
                            }
                        },
                        bgColor = if(currentDayIndex == index) Primary else Color.Transparent,
                        label = displayShortDayOfWeek(date, locale),
                        isLoading = availableDays is FeatureState.Loading,
                        isDayAvailable = isAvailable
                    )
                }
            }
        }

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
                SlotsList(
                    availableDayTimeslots = availableDayTimeslots,
                    onSelectSlot = onSelectSlot
                )
            }
        }
    }
}