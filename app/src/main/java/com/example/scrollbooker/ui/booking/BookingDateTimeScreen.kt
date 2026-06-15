package com.example.scrollbooker.ui.booking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.extensions.displayDatePeriod
import com.example.scrollbooker.core.extensions.displayShortDayOfWeek
import com.example.scrollbooker.core.util.AppLocaleProvider
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.shared.calendar.components.CalendarDayTab
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.headlineMedium
import kotlinx.coroutines.launch

@Composable
fun BookingDateTimeScreen(
    modifier: Modifier = Modifier,
    viewModel: BookingViewModel,
    onNavigateToConfirmation: () -> Unit,
    onBack: () -> Unit
) {
    val bookingTotals by viewModel.bookingTotals.collectAsStateWithLifecycle()
    val headerState by viewModel.calendarHeader.collectAsStateWithLifecycle()
    val timeSlots by viewModel.availableSlots.collectAsStateWithLifecycle()
    val selectedDay by viewModel.selectedDay.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    val totalWeeks = 26
    val weekPagerState = rememberPagerState(initialPage = 0) { totalWeeks }

    BookingLayout(
        modifier = modifier,
        onBack = onBack,
        onNext = onNavigateToConfirmation,
        bookingTotals = bookingTotals,
        displayBottomBar = true
    ) {
        when (val header = headerState) {
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Loading -> LoadingScreen()
            is FeatureState.Success -> {
                val calendar = header.data
                val calendarDays = calendar.calendarDays

                val availableDaysSet = remember(calendar.calendarAvailableDays) {
                    calendar.calendarAvailableDays.toSet()
                }

                if (calendarDays.isEmpty()) {
                    ErrorScreen()
                    return@BookingLayout
                }

                val currentWeekIndex = weekPagerState.currentPage
                val currentWeekDates = remember(currentWeekIndex, calendarDays) {
                    val startIndex = (currentWeekIndex * 7).coerceIn(0, calendarDays.lastIndex)
                    calendarDays.drop(startIndex).take(7)
                }

                val period = remember(currentWeekDates) {
                    if (currentWeekDates.isNotEmpty()) {
                        displayDatePeriod(currentWeekDates.first(), currentWeekDates.last())
                    } else ""
                }

                val enableBack = currentWeekIndex > 0
                val enableNext = currentWeekIndex < header.data.config.totalWeeks - 1

                fun handlePreviousWeek() {
                    scope.launch { weekPagerState.animateScrollToPage(currentWeekIndex - 1) }
                }

                fun handleNextWeek() {
                    scope.launch { weekPagerState.animateScrollToPage(currentWeekIndex + 1) }
                }

                val currentLocale = AppLocaleProvider.current()

                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = period,
                            style = headlineMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { if (enableBack) handlePreviousWeek() },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = if(enableBack) SurfaceBG else Color.Transparent,
                                    contentColor = if(enableBack) OnSurfaceBG.copy(0.8f) else Divider
                                )
                            ) {
                                Icon(
                                    modifier = Modifier.size(15.dp),
                                    imageVector = Icons.Default.ArrowBackIosNew,
                                    contentDescription = null,
                                )
                            }

                            Spacer(Modifier.width(SpacingXS))

                            IconButton(
                                onClick = { if (enableNext) handleNextWeek() },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = if(enableNext) SurfaceBG else Color.Transparent,
                                    contentColor = if(enableNext) OnSurfaceBG.copy(0.8f) else Divider
                                )
                            ) {
                                Icon(
                                    modifier = Modifier.size(15.dp),
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                    contentDescription = null,
                                )
                            }
                        }
                    }

                    HorizontalPager(
                        state = weekPagerState,
                        pageSize = PageSize.Fill,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(95.dp)
                            .padding(horizontal = BasePadding),
                        beyondViewportPageCount = 1
                    ) { page ->
                        val weekDates = remember(page, calendarDays) {
                            calendarDays.drop(page * 7).take(7)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            weekDates.forEach { date ->
                                val isAvailable = availableDaysSet.contains(date)
                                val isSelected = selectedDay == date

                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CalendarDayTab(
                                        date = date,
                                        isCurrentTab = isSelected,
                                        onChangeTab = {},
                                        bgColor = if (isSelected) Primary else Color.Transparent,
                                        label = remember(date, currentLocale) {
                                            displayShortDayOfWeek(date, currentLocale)
                                        },
                                        isLoading = false,
                                        isDayAvailable = isAvailable
                                    )
                                }
                            }
                        }
                    }

                    when(val slots = timeSlots) {
                        is FeatureState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
                        is FeatureState.Error -> ErrorScreen(modifier = Modifier.fillMaxSize())
                        is FeatureState.Success -> {
                            val slots = slots.data.availableSlots
                            if (slots.isEmpty()) {
                                Text(
                                    text = "No available time slots for the selected day.",
                                    modifier = Modifier.padding(BasePadding)
                                )
                            } else {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = "Available Time Slots",
                                        style = headlineMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = BasePadding)
                                    )

                                    Spacer(modifier = Modifier.height(SpacingXS))

                                    slots.forEach { slot ->
                                        Text(
                                            text = slot.startDateLocale,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    horizontal = BasePadding,
                                                    vertical = SpacingXS
                                                )
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