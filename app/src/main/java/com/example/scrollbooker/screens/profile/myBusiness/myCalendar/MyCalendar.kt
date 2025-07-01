package com.example.scrollbooker.screens.profile.myBusiness.myCalendar
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
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.modules.calendar.components.CalendarActions
import com.example.scrollbooker.modules.calendar.components.CalendarDayTab
import com.example.scrollbooker.modules.calendar.components.CalendarHeader
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import kotlinx.coroutines.launch
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.format.TextStyle
import java.util.Locale

@Composable
fun MyCalendarScreen(
    viewModel: MyCalendarViewModel,
    onBack: () -> Unit,
    isBusiness: Boolean = false
) {
    val locale = Locale("ro")
    val today = LocalDate.now()
    val totalWeeks = 13
    val initialPage = totalWeeks / 2

    val coroutineScope = rememberCoroutineScope()

    val weekPagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { totalWeeks }
    )
    val dayPagerState = rememberPagerState(
        initialPage = today.dayOfWeek.ordinal,
        pageCount = { 7 }
    )

    val currentWeekStart = today.with(DayOfWeek.MONDAY)
        .minusWeeks((initialPage - weekPagerState.currentPage).toLong())

    val currentWeekDays = remember(currentWeekStart) {
        (0..6).map { currentWeekStart.plusDays(it.toLong()) }
    }

    val period = "${currentWeekDays.first().dayOfMonth}-${currentWeekDays.last().dayOfMonth}" +
            " ${currentWeekStart.month.getDisplayName(TextStyle.FULL, locale)}"

    Column(
        modifier = Modifier
            .statusBarsPadding()
    ) {
        CalendarHeader(
            onBack = onBack,
            period = period
        )

        CalendarActions()

        HorizontalPager(
            state = weekPagerState,
            pageSize = PageSize.Fill,
            modifier = Modifier
                .fillMaxWidth()
                .height(95.dp)
                .padding(horizontal = BasePadding),
            userScrollEnabled = isBusiness || weekPagerState.currentPage >= initialPage
        ) { page ->
            val weekStart = today.with(DayOfWeek.MONDAY)
                .minusWeeks((initialPage - page).toLong())
            val weekDates = (0..6).map { weekStart.plusDays(it.toLong()) }

            val availableDays = listOf(0, 1, 3, 5, 7)

            TabRow(
                selectedTabIndex = dayPagerState.currentPage,
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.Transparent,
                contentColor = OnBackground,
                indicator = {},
            ) {
                weekDates.forEachIndexed { index, date ->
                    val dayLabel = date.dayOfWeek.getDisplayName(TextStyle.SHORT, locale)
                        .replaceFirstChar {
                            if(it.isLowerCase()) it.titlecase(locale) else it.toString()
                        }

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
                        label = dayLabel,
                        isDayAvailable = index in availableDays
                    )
                }
            }
        }

        HorizontalPager(
            state = dayPagerState,
            pageSize = PageSize.Fill,
            modifier = Modifier
                .fillMaxSize()
        ) { index ->
            val currentDate = currentWeekStart.plusDays(index.toLong())
            val slots = listOf(
                "09:00",
                "10:00",
                "11:00",
                "12:00",
                "13:00",
                "14:00",
                "15:00",
                "16:00",
                "17:00",
                "18:00",
                "19:00",
                "20:00",
                "21:00",
                "22:00",
                "23:00"
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = BasePadding),
                contentAlignment = Alignment.TopStart
            ) {
                LazyColumn {
                    items(slots) { slot ->
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
                                text = slot,
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