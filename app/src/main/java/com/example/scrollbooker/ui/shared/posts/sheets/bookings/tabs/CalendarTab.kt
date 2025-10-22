package com.example.scrollbooker.ui.shared.posts.sheets.bookings.tabs
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scrollbooker.entity.booking.calendar.domain.model.Slot
import com.example.scrollbooker.ui.shared.calendar.Calendar
import com.example.scrollbooker.ui.shared.calendar.CalendarViewModel

@Composable
fun CalendarTab(
    calendarViewModel: CalendarViewModel,
    slotDuration: Int,
    userId: Int,
    onSelectSlot: (Slot) -> Unit
) {
    Calendar(
        viewModel = calendarViewModel,
        userId = userId,
        productId = 11,
        slotDuration = slotDuration,
        onSelectSlot = onSelectSlot
    )
}