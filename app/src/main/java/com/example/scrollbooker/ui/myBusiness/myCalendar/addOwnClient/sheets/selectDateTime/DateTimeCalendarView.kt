package com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.sheets.selectDateTime
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.customized.calendar.CalendarHeaderState
import com.example.scrollbooker.core.util.AppLocaleProvider
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodySmall
import com.example.scrollbooker.ui.theme.titleMedium
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.format.TextStyle
import java.util.Locale

@Composable
fun DateTimeCalendarView(
    calendarHeaderState: FeatureState<CalendarHeaderState>,
    onDayClick: (LocalDate) -> Unit
) {
    when (val header = calendarHeaderState) {
        is FeatureState.Loading -> LoadingScreen()
        is FeatureState.Error -> ErrorScreen()
        is FeatureState.Success -> {
            val locale = AppLocaleProvider.current()
            val today = LocalDate.now()

            val availableDaysSet = remember(header.data.calendarAvailableDays) {
                header.data.calendarAvailableDays.toSet()
            }

            val monthGroups = remember(header.data.calendarDays) {
                val maxDay = today.plusMonths(6)

                header.data.calendarDays
                    .filter { it in today..maxDay }
                    .groupBy { it.year to it.monthValue }
                    .toList()
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = SpacingXL,
                    vertical = BasePadding
                ),
                horizontalArrangement = Arrangement.spacedBy(SpacingXS),
                verticalArrangement = Arrangement.spacedBy(SpacingXS)
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    WeekdayHeaderRow(locale)
                }

                monthGroups.forEach { (_, days) ->
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        MonthHeader(month = days.first(), locale = locale)
                    }

                    val leadingBlanks = days.first().dayOfWeek.value - 1
                    items(leadingBlanks) { }

                    items(days, key = { it.toString() }) { date ->
                        CalendarGridDayCell(
                            date = date,
                            isAvailable = availableDaysSet.contains(date),
                            isToday = date == today,
                            onClick = { onDayClick(date) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WeekdayHeaderRow(locale: Locale) {
    Row(Modifier.fillMaxWidth()) {
        for (i in 1..7) {
            val label = DayOfWeek.of(i)
                .getDisplayName(TextStyle.SHORT, locale)
                .replaceFirstChar { it.titlecase(locale) }

            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = SpacingS),
                text = label,
                style = bodySmall,
                color = Divider,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun MonthHeader(month: LocalDate, locale: Locale) {
    val label = month.month.getDisplayName(TextStyle.FULL, locale)
        .replaceFirstChar { it.titlecase(locale) }

    Text(
        modifier = Modifier.padding(top = BasePadding, bottom = SpacingS),
        text = "$label ${month.year}",
        style = titleMedium,
        color = OnBackground,
        fontWeight = FontWeight.ExtraBold
    )
}
