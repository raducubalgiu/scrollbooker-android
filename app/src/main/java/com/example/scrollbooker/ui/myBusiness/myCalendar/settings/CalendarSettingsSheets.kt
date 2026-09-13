package com.example.scrollbooker.ui.myBusiness.myCalendar.settings

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.ui.theme.Background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarSettingsSheets(
    currentSheet: CalendarSettingsSheet?,
    sheetState: SheetState,
    slotDuration: Int,
    gapMinutes: Int,
    isSaving: Boolean,
    onSaveSlotDuration: (Int) -> Unit,
    onSaveGap: (Int) -> Unit,
    onClose: () -> Unit
) {
    if (currentSheet == null) return

    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        sheetState = sheetState,
        onDismissRequest = onClose,
        containerColor = Background,
        dragHandle = {}
    ) {
        when (currentSheet) {
            CalendarSettingsSheet.Duration -> DurationPickerSheet(
                title = stringResource(R.string.slotDurationSectionTitle),
                options = SLOT_DURATION_OPTIONS,
                selectedMinutes = slotDuration,
                isSaving = isSaving,
                onSave = onSaveSlotDuration,
                onClose = onClose
            )

            CalendarSettingsSheet.Gap -> DurationPickerSheet(
                title = stringResource(R.string.appointmentGapSectionTitle),
                options = GAP_OPTIONS,
                selectedMinutes = gapMinutes,
                isSaving = isSaving,
                onSave = onSaveGap,
                onClose = onClose
            )

            CalendarSettingsSheet.GoogleCalendar -> GoogleCalendarSheet(onClose = onClose)
        }
    }
}
