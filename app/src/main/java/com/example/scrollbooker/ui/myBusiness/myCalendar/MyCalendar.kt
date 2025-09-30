package com.example.scrollbooker.ui.myBusiness.myCalendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.displayDatePeriod
import com.example.scrollbooker.core.util.displayShortDayOfWeek
import com.example.scrollbooker.entity.booking.calendar.domain.model.timeFromLocale
import com.example.scrollbooker.ui.modules.calendar.components.CalendarDayTab
import com.example.scrollbooker.ui.modules.calendar.components.CalendarHeader
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.DayTimeline
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyCalendarScreen(
    viewModel: MyCalendarViewModel,
    onBack: () -> Unit
) {
    val headerState by viewModel.calendarHeader.collectAsState()
    val slotDuration by viewModel.slotDuration.collectAsState()

    val calendarEvents by viewModel.calendarEvents.collectAsState()

    val locale = Locale("ro")
    val coroutineScope = rememberCoroutineScope()

    fun handleDayChange(day: LocalDate) {
        viewModel.setDay(day)
    }

    Scaffold(
        topBar = {
            Header(
                title = stringResource(R.string.calendar),
                onBack = onBack
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = innerPadding.calculateTopPadding())
        ) {
            when(headerState) {
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Success -> {
                    val calendar = (headerState as FeatureState.Success).data
                    val config = calendar.config
                    val calendarDays = calendar.calendarDays
                    val availableDays = calendar.calendarAvailableDays

                    if(calendar.calendarDays.isEmpty()) {
                        ErrorScreen()
                        return@Box
                    }

                    val weekPagerState = rememberPagerState(initialPage = config.initialWeekPage) { config.totalWeeks }
                    val dayPagerState = rememberPagerState(initialPage = config.initialDayPage) { 7 }

                    val currentDayIndex = dayPagerState.currentPage
                    val currentWeekIndex = weekPagerState.currentPage

                    LaunchedEffect(currentWeekIndex, currentDayIndex) {
                        val dayIndex = currentWeekIndex * 7 + currentDayIndex
                        val day = calendarDays.getOrNull(dayIndex)
                        if(day != null) {
                            handleDayChange(day)
                        }
                    }

                    val currentWeekDates = calendarDays.drop(currentWeekIndex * 7).take(7)
                    val enableBack = currentWeekIndex > 0
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

                    Column(modifier = Modifier.fillMaxSize()) {
                        Box(Modifier.fillMaxWidth().padding(horizontal = BasePadding)) {
                            InputSelect(
                                options = durations,
                                label = "Interval",
                                placeholder = "Alege intervalul",
                                selectedOption = slotDuration.toString(),
                                onValueChange = {
                                    viewModel.setSlotDuration(it)
                                },
                            )
                        }

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
                                    val isAvailable = availableDays.contains(date) == true

                                    CalendarDayTab(
                                        date = date,
                                        isCurrentTab = index == dayPagerState.currentPage,
                                        onChangeTab = {
                                            coroutineScope.launch {
                                                handleDayChange(date)
                                                dayPagerState.animateScrollToPage(index)
                                            }
                                        },
                                        bgColor = if(currentDayIndex == index) Primary else Color.Transparent,
                                        label = displayShortDayOfWeek(date, locale),
                                        isLoading = false,
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
                                when(val events = calendarEvents) {
                                    is FeatureState.Error -> { ErrorScreen() }
                                    is FeatureState.Loading -> { LoadingScreen() }
                                    is FeatureState.Success -> {
                                        val startEnd = events.data.timeFromLocale()
                                        val slots = events.data.days.first().slots

                                        if(startEnd != null) {
                                            DayTimeline(
                                                slotDuration = slotDuration,
                                                slots = slots,
                                                dayStart = startEnd.minSlotTime,
                                                dayEnd = startEnd.maxSlotTime
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}