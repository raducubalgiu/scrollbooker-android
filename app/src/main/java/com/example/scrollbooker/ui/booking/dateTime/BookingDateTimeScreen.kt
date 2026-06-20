package com.example.scrollbooker.ui.booking.dateTime
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.extensions.displayDatePeriod
import com.example.scrollbooker.core.extensions.displayShortDayOfWeek
import com.example.scrollbooker.core.util.AppLocaleProvider
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.navigators.BookingNavigator
import com.example.scrollbooker.ui.booking.BookingLayout
import com.example.scrollbooker.ui.booking.BookingViewModel
import com.example.scrollbooker.ui.shared.calendar.components.CalendarDayTab
import com.example.scrollbooker.ui.shared.calendar.components.slots.FullyBookedDayMessage
import com.example.scrollbooker.ui.shared.calendar.components.slots.SlotItem
import com.example.scrollbooker.ui.shared.calendar.components.slots.SlotsShimmer
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.headlineLarge
import com.example.scrollbooker.ui.theme.titleLarge
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

@Composable
fun BookingDateTimeScreen(
    modifier: Modifier = Modifier,
    viewModel: BookingViewModel,
    bookingNavigate: BookingNavigator
) {
    val bookingTotals by viewModel.bookingTotals.collectAsStateWithLifecycle()
    val headerState by viewModel.calendarHeader.collectAsStateWithLifecycle()
    val timeSlots by viewModel.availableSlots.collectAsStateWithLifecycle()
    val selectedDay by viewModel.selectedDay.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    val totalWeeks = 26
    val totalDays = totalWeeks * 7
    val weekPagerState = rememberPagerState(initialPage = 0) { totalWeeks }
    val dayPagerState = rememberPagerState(initialPage = 0) { totalDays }

    BookingLayout(
        modifier = modifier,
        onBack = { bookingNavigate.back() },
        onNext = { bookingNavigate.toConfirmation() },
        bookingTotals = bookingTotals,
        displayBottomBar = false
    ) {
        Text(
            modifier = Modifier.padding(BasePadding),
            style = headlineLarge,
            color = OnBackground,
            fontWeight = FontWeight.ExtraBold,
            text = "Alege Ora"
        )

        when (val header = headerState) {
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Loading -> LoadingScreen()
            is FeatureState.Success -> {
                val calendar = header.data
                val calendarDays = calendar.calendarDays
                val config = calendar.config

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
                val enableNext = currentWeekIndex < config.totalWeeks - 1

                fun handlePreviousWeek() {
                    scope.launch { weekPagerState.animateScrollToPage(currentWeekIndex - 1) }
                }

                fun handleNextWeek() {
                    scope.launch { weekPagerState.animateScrollToPage(currentWeekIndex + 1) }
                }

                val currentLocale = AppLocaleProvider.current()

                LaunchedEffect(weekPagerState.currentPage, dayPagerState.currentPage) {
                    val today = LocalDate.now()
                    val calendar =
                        (headerState as? FeatureState.Success)?.data ?: return@LaunchedEffect
                    val calendarDays = calendar.calendarDays

                    if (dayPagerState.isScrollInProgress) {
                        val currentDayIndex = dayPagerState.currentPage
                        val targetWeekPage = currentDayIndex / 7

                        if (weekPagerState.currentPage != targetWeekPage) {
                            weekPagerState.scrollToPage(targetWeekPage)
                        }

                        calendarDays.getOrNull(currentDayIndex)?.let { targetDate ->
                            val finalDate = if (targetDate.isBefore(today)) today else targetDate
                            if (finalDate != selectedDay) {
                                viewModel.onDaySelected(finalDate)
                            }
                        }
                    } else if (weekPagerState.isScrollInProgress) {
                        val targetWeekIndex = weekPagerState.currentPage

                        val currentDayOfWeekOrdinal = selectedDay.dayOfWeek.ordinal

                        val targetDayIndex = targetWeekIndex * 7 + currentDayOfWeekOrdinal
                        val targetDate = calendarDays.getOrNull(targetDayIndex)

                        if (targetDate != null) {
                            val finalDate = if (targetDate.isBefore(today)) today else targetDate

                            val finalDayIndex = calendarDays.indexOf(finalDate).coerceAtLeast(0)
                            if (dayPagerState.currentPage != finalDayIndex) {
                                dayPagerState.scrollToPage(finalDayIndex)
                            }

                            if (finalDate != selectedDay) {
                                viewModel.onDaySelected(finalDate)
                            }
                        }
                    }
                }

                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = BasePadding),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                tint = Color.Gray,
                                painter = painterResource(R.drawable.ic_calendar_outline_stroke_small),
                                contentDescription = null
                            )

                            Spacer(Modifier.width(SpacingS))

                            Text(
                                text = period,
                                style = titleLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { if (enableBack) handlePreviousWeek() },
                                modifier = Modifier.border(
                                    width = 1.dp,
                                    color = if(enableBack) Divider else Color.Transparent,
                                    shape = CircleShape
                                ),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = if (enableBack) OnSurfaceBG.copy(alpha = 0.8f) else Divider
                                )
                            ) {
                                Icon(
                                    modifier = Modifier.size(15.dp),
                                    imageVector = Icons.Default.ArrowBackIosNew,
                                    contentDescription = null,
                                )
                            }

                            Spacer(Modifier.width(SpacingS))

                            IconButton(
                                onClick = { if (enableNext) handleNextWeek() },
                                modifier = Modifier.border(
                                    width = 1.dp,
                                    color = if(enableNext) Divider else Color.Transparent,
                                    shape = CircleShape
                                ),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = if (enableNext) OnSurfaceBG.copy(alpha = 0.8f) else Divider
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

                    Spacer(Modifier.height(SpacingS))

                    HorizontalPager(
                        state = weekPagerState,
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
                            weekDates.forEachIndexed { index, date ->
                                val isAvailable = availableDaysSet.contains(date)
                                val isSelected = selectedDay == date

                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CalendarDayTab(
                                        date = date,
                                        isCurrentTab = isSelected,
                                        onChangeTab = {
                                            val targetDayIndex = page * 7 + index
                                            scope.launch {
                                                dayPagerState.animateScrollToPage(
                                                    targetDayIndex
                                                )
                                            }
                                        },
                                        bgColor = if (isSelected) Primary else Color.Transparent,
                                        label = remember(
                                            date,
                                            currentLocale
                                        ) { displayShortDayOfWeek(date, currentLocale) },
                                        isLoading = false,
                                        isDayAvailable = isAvailable
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(BasePadding))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        HorizontalPager(
                            state = dayPagerState,
                            modifier = Modifier.fillMaxWidth(),
                            beyondViewportPageCount = 1
                        ) { pageIndex ->
                            when (val slots = timeSlots) {
                                is FeatureState.Loading -> SlotsShimmer()
                                is FeatureState.Error -> ErrorScreen()
                                is FeatureState.Success -> {
                                    val availableSlotsList = slots.data.availableSlots

                                    if (availableSlotsList.isEmpty()) {
                                        FullyBookedDayMessage(onClick = {})
                                    } else {
                                        LazyColumn(
                                            modifier = Modifier.fillMaxSize(),
                                            contentPadding = PaddingValues(bottom = BasePadding),
                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            items(availableSlotsList) { slot ->
                                                SlotItem(
                                                    slot = slot,
                                                    onSelectSlot = {
                                                        viewModel.onSlotSelected(slot)
                                                        bookingNavigate.toConfirmation()
                                                    }
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
}