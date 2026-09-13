package com.example.scrollbooker.ui.myBusiness.myCalendar.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.ui.myBusiness.myCalendar.MyCalendarViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCalendarSettingsScreen(
    myCalendarViewModel: MyCalendarViewModel,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val slotDuration by myCalendarViewModel.slotDuration.collectAsStateWithLifecycle()
    val gapMinutes by myCalendarViewModel.appointmentGapMinutes.collectAsStateWithLifecycle()
    val isSavingCalendarSettings by myCalendarViewModel.isSavingCalendarSettings.collectAsStateWithLifecycle()
    val canSetAppointmentGap by myCalendarViewModel.canSetAppointmentGap.collectAsStateWithLifecycle()

    var currentSheet by remember { mutableStateOf<CalendarSettingsSheet?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    fun closeSheet() {
        scope.launch {
            sheetState.hide()
            currentSheet = null
        }
    }

    CalendarSettingsSheets(
        currentSheet = currentSheet,
        sheetState = sheetState,
        slotDuration = slotDuration,
        gapMinutes = gapMinutes,
        isSaving = isSavingCalendarSettings,
        onSaveSlotDuration = { myCalendarViewModel.saveSlotDuration(it) },
        onSaveGap = { myCalendarViewModel.saveAppointmentGap(it) },
        onClose = { closeSheet() }
    )

    Layout(
        headerTitle = stringResource(R.string.calendarSettings),
        onBack = onBack,
        enablePaddingH = false
    ) {
        val slotDurationLabel = SLOT_DURATION_OPTIONS.labelResFor(slotDuration)?.let { stringResource(it) } ?: ""
        val gapLabel = GAP_OPTIONS.labelResFor(gapMinutes)?.let { stringResource(it) } ?: ""
        val connectionLabel = rememberGoogleCalendarStatusLabel()

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CalendarSettingsRow(
                titleRes = R.string.slotDurationSectionTitle,
                descriptionRes = R.string.slotDurationSectionDescription,
                value = slotDurationLabel,
                onClick = { currentSheet = CalendarSettingsSheet.Duration }
            )

            if (canSetAppointmentGap) {
                CalendarSettingsRow(
                    titleRes = R.string.appointmentGapSectionTitle,
                    descriptionRes = R.string.appointmentGapSectionDescription,
                    value = gapLabel,
                    onClick = { currentSheet = CalendarSettingsSheet.Gap }
                )
            }

            CalendarSettingsRow(
                titleRes = R.string.calendarConnection,
                descriptionRes = R.string.calendarConnectionDescription,
                value = connectionLabel,
                onClick = { currentSheet = CalendarSettingsSheet.GoogleCalendar }
            )
        }
    }
}
