package com.example.scrollbooker.ui.modules.posts.sheets.calendar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scrollbooker.ui.shared.calendar.CalendarViewModel

@Composable
fun PostCalendarSheet() {
    val viewModel: CalendarViewModel = hiltViewModel()

    val selectedSlot by viewModel.selectedSlot.collectAsState()
    val targetState = if(selectedSlot == null) PostCalendarState.CALENDAR else PostCalendarState.CONFIRM

    PostCalendarHeader(targetState = targetState)

    PostCalendarBody(
        targetState = targetState,
        viewModel = viewModel
    )
}