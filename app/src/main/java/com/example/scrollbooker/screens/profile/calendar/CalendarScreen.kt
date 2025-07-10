package com.example.scrollbooker.screens.profile.calendar

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.modules.calendar.Calendar
import com.example.scrollbooker.modules.calendar.CalendarViewModel

@Composable
fun CalendarScreen(
    userId: Int,
    viewModel: CalendarViewModel,
    onBack: () -> Unit
) {
    val config by viewModel.calendarConfig.collectAsState()
    val calendarDays by viewModel.calendarDays.collectAsState()
    val availableDays by viewModel.availableDays.collectAsState()
    val availableDayTimeslots by viewModel.availableDay.collectAsState()
    val selectedSlot by viewModel.selectedSlot.collectAsState()

    LaunchedEffect(Unit) { viewModel.setCalendarConfig(userId) }

    LaunchedEffect(config?.selectedDay) {
        config?.let {
            viewModel.loadUserAvailableTimeslots(
                userId = it.userId,
                day = it.selectedDay,
                slotDuration = 30
            )
        }
    }

    Layout(
        modifier = Modifier.statusBarsPadding(),
        onBack = onBack,
        headerTitle = stringResource(R.string.calendar),
        enablePaddingH = false
    ) {
        Calendar(
            availableDayTimeslots = availableDayTimeslots,
            calendarDays = calendarDays,
            availableDays = availableDays,
            config = config,
            onDayChange = { viewModel.updateSelectedDay(it) },
            onSelectSlot = { viewModel.toggleSlot(it) }
        )
    }
}