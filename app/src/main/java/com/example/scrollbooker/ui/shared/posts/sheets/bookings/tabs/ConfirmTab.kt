package com.example.scrollbooker.ui.shared.posts.sheets.bookings.tabs
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.entity.booking.calendar.domain.model.Slot

@Composable
fun ConfirmTab(
    selectedSlot: Slot?
) {
    Column(Modifier.fillMaxSize()) {
        selectedSlot?.let { slot ->
            Text(text = slot.startDateLocale)
            Text(text = slot.endDateLocale)
        }
    }
}