package com.example.scrollbooker.ui.myBusiness.myCalendar.components.sheets.settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCalendarSettingsSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {
        Text("Settings Sheet")
        Text("Settings Sheet")
        Text("Settings Sheet")
        Text("Settings Sheet")
        Text("Settings Sheet")
        Text("Settings Sheet")
    }
}