package com.example.scrollbooker.ui.shared.posts.sheets.products.tabs
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scrollbooker.entity.booking.calendar.domain.model.Slot
import com.example.scrollbooker.ui.shared.calendar.Calendar
import com.example.scrollbooker.ui.shared.calendar.CalendarViewModel

@Composable
fun CalendarTab(
    userId: Int,
    onSelectSlot: (Slot) -> Unit
) {
    val viewModel: CalendarViewModel = hiltViewModel()

    Calendar(
        viewModel = viewModel,
        userId = userId,
        productId = 11,
        slotDuration = 30,
        onSelectSlot = onSelectSlot
    )
}