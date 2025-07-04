package com.example.scrollbooker.screens.calendar

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.screens.calendar.components.ExpandableCalendar
import com.example.scrollbooker.screens.calendar.components.WeeklyHeader
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel,
    onBack: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = Int.MAX_VALUE / 2) { 7 }
    val referenceDate = LocalDate.now()

    Layout(
        headerTitle = stringResource(R.string.calendar),
        onBack = onBack,
        enablePaddingH = false
    ) {
//        HorizontalPager(
//            state = pagerState
//        ) { pageIndex ->
//            val weekStart = referenceDate
//                .plusWeeks((pageIndex - Int.MAX_VALUE / 2).toLong())
//                .with(DayOfWeek.MONDAY)
//            val daysOfWeek = (0..6).map { weekStart.plusDays(it.toLong()) }
//
//            var selectedDayIndex by remember { mutableStateOf(0) }
//
//            val availableDays = listOf(
//                "2025-06-23",
//                "2025-06-24",
//                "2025-06-25",
//                "2025-06-26",
//                "2025-06-27",
//                "2025-06-28",
//                "2025-06-29"
//            )
//
//            Column {
//                TabRow(selectedTabIndex = selectedDayIndex) {
//                    daysOfWeek.forEachIndexed { index, day ->
//                        Tab(
//                            selected = selectedDayIndex == index,
//                            onClick = { selectedDayIndex == index },
//                            text = {
//                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                                    Text(
//                                        text = day.format(DateTimeFormatter.ofPattern("dd")),
//                                        style = labelMedium
//                                    )
//                                    Box(modifier = Modifier
//                                        .padding(top = 4.dp)
//                                        .size(6.dp)
//                                        .clip(CircleShape)
//                                        .background(Color.Green)
//                                    )
//                                }
//                            }
//                        )
//                    }
//                }
//                Column(modifier = Modifier.fillMaxWidth()) {
//                    Box(modifier = Modifier.fillMaxWidth().padding(SpacingS), contentAlignment = Alignment.Center) {
//                        Icon(Icons.Default.ExpandMore, contentDescription = null)
//                    }
//                }
//                Column(modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.Red),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Text("Hello World")
//                }
//            }
//        }

        val today = LocalDate.now()

        val dummyAvailableDays = remember {
            setOf(
                today.plusDays(1),
                today.plusDays(2),
                today.plusDays(5),
                today.plusDays(9),
                today.plusDays(15),
            )
        }

        var selectedDate by remember { mutableStateOf(today) }
        var isCalendarExpanded by remember { mutableStateOf(false) }

        val weekStart = selectedDate.with(DayOfWeek.MONDAY)
        val weekDates = (0..6).map { weekStart.plusDays(it.toLong()) }

        var selectedDayIndex by remember {
            mutableStateOf(weekDates.indexOfFirst { it == selectedDate }.coerceAtLeast(0))
        }

        Column {
            WeeklyHeader(
                isCalendarExpanded = isCalendarExpanded,
                weekDates = weekDates,
                availableDays = dummyAvailableDays,
                selectedIndex = selectedDayIndex,
                onDayClick = { index ->
                    selectedDayIndex = index
                    selectedDate = weekDates[index]
                },
                onToggleCalendar = {
                    isCalendarExpanded = !isCalendarExpanded
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(
                        animationSpec = tween(durationMillis = 500)
                    )
            ) {
                if(isCalendarExpanded) {
                    ExpandableCalendar(
                        availableDates = dummyAvailableDays,
                        selectedDate = selectedDate,
                        onDateSelected = { date ->
                            selectedDate = date
                            selectedDayIndex = (0..6).firstOrNull { weekDates[it] == date } ?: 0
                            isCalendarExpanded = false
                        },
                    )
                }
            }
        }
    }
}