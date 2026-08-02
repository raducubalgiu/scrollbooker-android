package com.example.scrollbooker.ui.booking.dateTime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.extensions.displayShortDayOfWeek
import com.example.scrollbooker.core.util.AppLocaleProvider
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.shared.calendar.components.CalendarDayTab
import com.example.scrollbooker.ui.theme.Primary
import org.threeten.bp.LocalDate

@Composable
fun HeaderPager(
    weekPagerState: PagerState,
    calendarDays: List<LocalDate>,
    availableDaysSet: Set<LocalDate>,
    selectedDay: LocalDate,
    onChangeTab: (Int) -> Unit
) {
    val currentLocale = AppLocaleProvider.current()

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
                            onChangeTab(targetDayIndex)
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
}