package com.example.scrollbooker.modules.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.shimmer.rememberShimmerBrush
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.modules.calendar.components.CalendarActions
import com.example.scrollbooker.modules.calendar.components.CalendarDayTab
import com.example.scrollbooker.screens.profile.myBusiness.myCalendar.rememberAnimatedPagerStyle
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.util.Locale
import com.example.scrollbooker.core.util.displayShortDayOfWeek
import com.example.scrollbooker.core.util.formatHour
import com.example.scrollbooker.entity.calendar.domain.model.AvailableDay

@Composable
fun Calendar(
    availableDayTimeslots: FeatureState<AvailableDay>,
    calendarDays: List<LocalDate>,
    availableDays: FeatureState<List<LocalDate>>,
    onBack: (() -> Unit)? = null,
    config: CalendarConfig?,
    onDayChange: (LocalDate) -> Unit,
    onPeriodChange: (LocalDate, LocalDate, Locale) -> Unit
) {
    if(calendarDays.isEmpty() || config == null) return

    val locale = Locale("ro")
    val coroutineScope = rememberCoroutineScope()

    val weekPagerState = rememberPagerState(initialPage = config.initialWeekPage) { config.totalWeeks }
    val dayPagerState = rememberPagerState(initialPage = config.initialDayPage) { 7 }

    val currentDayIndex = dayPagerState.currentPage
    val currentWeekIndex = weekPagerState.currentPage

    val currentWeekDates = calendarDays.drop(currentWeekIndex * 7).take(7)

    val enableBack = currentWeekIndex > 0
    val enableNext = currentWeekIndex < config.totalWeeks - 1

    LaunchedEffect(currentWeekIndex) {
        onPeriodChange(currentWeekDates.first(), currentWeekDates.last(), locale)
    }

    fun handlePreviousWeek() {
        if(!enableBack) return
        coroutineScope.launch {
            weekPagerState.animateScrollToPage(currentWeekIndex - 1)
        }
    }

    fun handleNextWeek() {
        if(!enableNext) return
        coroutineScope.launch {
            weekPagerState.animateScrollToPage(currentWeekIndex + 1)
        }
    }

    val selectedDay = calendarDays.drop(currentWeekIndex * 7).getOrNull(currentDayIndex)

    val previousDayPage = remember { mutableIntStateOf(dayPagerState.currentPage) }
    val justSwitchedWeek = remember { mutableStateOf(false) }
    val directionState = remember { mutableIntStateOf(0) }

    LaunchedEffect(dayPagerState.currentPage) {
        if (justSwitchedWeek.value) {
            justSwitchedWeek.value = false
            previousDayPage.intValue = dayPagerState.currentPage
            return@LaunchedEffect
        }

        directionState.intValue = dayPagerState.currentPage - previousDayPage.intValue

        val isLastDay = dayPagerState.currentPage == 6
        val isFirstDay = dayPagerState.currentPage == 0

        if(directionState.intValue > 0 && isLastDay && enableNext) {
            justSwitchedWeek.value = true

            coroutineScope.launch {
                weekPagerState.animateScrollToPage(currentWeekIndex + 1)
                dayPagerState.animateScrollToPage(0)
            }

        } else if (directionState.intValue < 0 && isFirstDay && enableBack) {
            justSwitchedWeek.value = true

            coroutineScope.launch {
                weekPagerState.animateScrollToPage(currentWeekIndex - 1)
                dayPagerState.animateScrollToPage(6)
            }
        }

        previousDayPage.intValue = dayPagerState.currentPage
    }

    LaunchedEffect(currentWeekIndex, currentDayIndex) {
        selectedDay?.let {
            onDayChange(it)
        }
    }

    Column(modifier = Modifier.statusBarsPadding()) {
//        CalendarHeader(
//            onBack = onBack,
//            period = period
//        )

        CalendarActions(
            enableBack = enableBack,
            enableNext = enableNext,
            onPreviousWeek = { handlePreviousWeek() },
            onNextWeek = { handleNextWeek() }
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
            modifier = Modifier
                .fillMaxSize()
        ) { index ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = BasePadding),
                contentAlignment = Alignment.TopStart
            ) {
                when(val day = availableDayTimeslots) {
                    is FeatureState.Error -> ErrorScreen()
                    is FeatureState.Loading -> {
                        Column(Modifier.fillMaxSize()) {
                            repeat(10) {
                                val brush = rememberShimmerBrush()

                                Spacer(Modifier.height(BasePadding))

                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .clip(shape = ShapeDefaults.Medium)
                                        .background(brush)
                                        .padding(BasePadding)
                                )
                            }
                        }
                    }
                    is FeatureState.Success -> {
                        val day = day.data

                        LazyColumn {
                            items(day.availableSlots) { slot ->
                                Spacer(Modifier.height(BasePadding))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(shape = ShapeDefaults.Medium)
                                        .background(SurfaceBG)
                                        .clickable {}
                                        .padding(BasePadding),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = formatHour(slot.startDateLocale),
                                        style = bodyLarge,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
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
}