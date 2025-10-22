package com.example.scrollbooker.ui.shared.calendar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.extensions.displayDatePeriod
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.util.Locale
import com.example.scrollbooker.core.extensions.displayShortDayOfWeek
import com.example.scrollbooker.entity.booking.calendar.domain.model.Slot
import com.example.scrollbooker.ui.shared.calendar.components.CalendarDayTab
import com.example.scrollbooker.ui.shared.calendar.components.CalendarHeader
import com.example.scrollbooker.ui.shared.calendar.components.slots.SlotsList

@Composable
fun Calendar(
    viewModel: CalendarViewModel,
    userId: Int,
    productId: Int,
    slotDuration: Int,
    onSelectSlot: (Slot) -> Unit
) {
    LaunchedEffect(userId) {
        viewModel.setUserId(userId)
    }

    LaunchedEffect(Unit) {
        viewModel.setSlotDuration(duration = slotDuration)
        viewModel.loadProduct(productId)
    }

    fun handleDayChange(day: LocalDate) {
        viewModel.setDay(day)
    }

    val headerState by viewModel.calendarHeader.collectAsState()

    val locale = Locale("ro")
    val coroutineScope = rememberCoroutineScope()
    val today = LocalDate.now()

    when(headerState) {
        is FeatureState.Error -> ErrorScreen()
        is FeatureState.Loading -> LoadingScreen()
        is FeatureState.Success -> {
            val calendar = (headerState as FeatureState.Success).data
            val config = calendar.config
            val calendarDays = calendar.calendarDays
            val availableDays = calendar.calendarAvailableDays

            if(calendar.calendarDays.isEmpty()) return

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

            Column {
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
                        SlotsList(
                            viewModel = viewModel,
                            onSelectSlot = {
                                viewModel.setSelectedSlot(it)
                                onSelectSlot(it)
                            }
                        )
                    }
                }
            }
        }
    }
}