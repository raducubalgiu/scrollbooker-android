package com.example.scrollbooker.ui.booking.dateTime
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.extensions.displayDatePeriod
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.navigators.BookingNavigator
import com.example.scrollbooker.ui.booking.BookingLayout
import com.example.scrollbooker.ui.booking.BookingViewModel
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.headlineLarge
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
                    BookingDateTimeActions(
                        period = period,
                        enableBack = enableBack,
                        enableNext = enableNext,
                        handlePreviousWeek = ::handlePreviousWeek,
                        handleNextWeek = ::handleNextWeek
                    )

                    Spacer(Modifier.height(SpacingS))

                    HeaderPager(
                        weekPagerState = weekPagerState,
                        calendarDays = calendarDays,
                        availableDaysSet = availableDaysSet,
                        selectedDay = selectedDay,
                        onChangeTab = { targetDayIndex ->
                            scope.launch {
                                dayPagerState.animateScrollToPage(targetDayIndex)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(BasePadding))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        ContentPager(
                            dayPagerState = dayPagerState,
                            timeSlots = timeSlots,
                            onSlotSelected = { slot ->
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