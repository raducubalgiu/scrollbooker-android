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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.modules.calendar.components.CalendarActions
import com.example.scrollbooker.modules.calendar.components.CalendarDayTab
import com.example.scrollbooker.modules.calendar.components.CalendarHeader
import com.example.scrollbooker.screens.profile.myBusiness.myCalendar.rememberAnimatedPagerStyle
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.util.Locale
import com.example.scrollbooker.core.util.displayDatePeriod
import com.example.scrollbooker.core.util.displayShortDayOfWeek
import com.example.scrollbooker.entity.calendar.domain.model.AvailableDay

@Composable
fun Calendar(
    availableDayTimeslots: FeatureState<AvailableDay>,
    calendarDays: List<LocalDate>,
    availableDays: FeatureState<List<LocalDate>>,
    onBack: () -> Unit,
    config: CalendarConfig?
) {
    if(calendarDays.isEmpty() || config == null) return

    val locale = Locale("ro")
    val coroutineScope = rememberCoroutineScope()

    val weekPagerState = rememberPagerState(initialPage = config.initialWeekPage) { config.totalWeeks }
    val dayPagerState = rememberPagerState(initialPage = config.initialDayPage) { 7 }
    val currentWeekIndex = weekPagerState.currentPage

    val currentWeekDates = calendarDays.drop(currentWeekIndex * 7).take(7)
    val period = displayDatePeriod(start = currentWeekDates.first(), end = currentWeekDates.last(), locale)

    val enableBack = currentWeekIndex > 0
    val enableNext = currentWeekIndex < config.totalWeeks -1

    fun handlePreviousWeek() {
        coroutineScope.launch {
            if(enableBack) {
                weekPagerState.scrollToPage(currentWeekIndex - 1)
            }
        }
    }

    fun handleNextWeek() {
        coroutineScope.launch {
            if(enableNext) {
                weekPagerState.scrollToPage(currentWeekIndex + 1)
            }
        }
    }

    Column(modifier = Modifier.statusBarsPadding()) {
        CalendarHeader(
            onBack = onBack,
            period = period
        )

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

                    val (bgColor, scale) = rememberAnimatedPagerStyle(
                        pagerState = dayPagerState,
                        index = index,
                        primaryColor = Primary
                    )

                    CalendarDayTab(
                        date = date,
                        isCurrentTab = index == dayPagerState.currentPage,
                        onChangeTab = {
                            coroutineScope.launch {
                                dayPagerState.animateScrollToPage(index)
                            }
                        },
                        bgColor = bgColor,
                        scale = scale,
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
                    is FeatureState.Loading -> LoadingScreen()
                    is FeatureState.Success -> {
                        val day = day.data

                        LazyColumn {
                            items(day.slots) { slot ->
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
                                        text = slot.startDateLocale,
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