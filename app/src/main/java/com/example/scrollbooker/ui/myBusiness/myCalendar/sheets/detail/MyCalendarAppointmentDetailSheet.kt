package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.detail

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCalendarAppointmentDetailSheet(
    sheetState: SheetState,
    onClose: () -> Unit
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onClose
    ) {
        Text("Settings Sheet")
        Text("Settings Sheet")
        Text("Settings Sheet")
        Text("Settings Sheet")
        Text("Settings Sheet")
        Text("Settings Sheet")
    }
}