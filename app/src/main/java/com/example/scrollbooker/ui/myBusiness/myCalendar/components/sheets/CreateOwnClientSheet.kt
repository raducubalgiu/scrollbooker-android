package com.example.scrollbooker.ui.myBusiness.myCalendar.components.sheets

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsSlot

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOwnClientSheet(
    sheetState: SheetState,
    selectedOwnClientSlot: CalendarEventsSlot?,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {
        selectedOwnClientSlot?.startDateUtc?.let {
            Text(it)
        }
    }
}