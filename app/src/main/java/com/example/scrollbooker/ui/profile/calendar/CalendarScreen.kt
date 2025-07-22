package com.example.scrollbooker.ui.profile.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.modules.calendar.Calendar
import com.example.scrollbooker.modules.calendar.CalendarViewModel
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun CalendarScreen(
    userId: Int,
    slotDuration: Int,
    productId: Int,
    productName: String,
    viewModel: CalendarViewModel,
    onBack: () -> Unit,
    onNavigateToConfirmation: () -> Unit
) {
    val config by viewModel.calendarConfig.collectAsState()
    val calendarDays by viewModel.calendarDays.collectAsState()
    val availableDays by viewModel.availableDays.collectAsState()
    val availableDayTimeslots by viewModel.availableDay.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.setCalendarConfig(userId)
    }

    LaunchedEffect(config?.selectedDay) {
        config?.let {
            viewModel.loadUserAvailableTimeslots(
                userId = it.userId,
                day = it.selectedDay,
                slotDuration = slotDuration
            )
        }
    }

    Column (
        modifier = Modifier
            .statusBarsPadding()
            .padding(
                bottom = WindowInsets
                    .systemBars
                    .asPaddingValues()
                    .calculateBottomPadding()
            )
    ) {
        Header(
            onBack = onBack,
            customTitle = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        style = titleMedium,
                        color = OnBackground,
                        fontWeight = FontWeight.Bold,
                        text = stringResource(R.string.calendar)
                    )
                    Text(
                        style = titleMedium,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                        text = productName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        )

        Calendar(
            availableDayTimeslots = availableDayTimeslots,
            calendarDays = calendarDays,
            availableDays = availableDays,
            config = config,
            onDayChange = { viewModel.updateSelectedDay(it) },
            onSelectSlot = {
                viewModel.toggleSlot(it)
                onNavigateToConfirmation()
            }
        )
    }
}