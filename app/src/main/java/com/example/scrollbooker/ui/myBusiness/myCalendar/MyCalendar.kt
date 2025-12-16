package com.example.scrollbooker.ui.myBusiness.myCalendar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.block.BlockSlotsAction
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.block.BlockSlotsSheet
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.block.BlockSlotsSheetState
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.settings.MyCalendarSettingsSheet
import kotlinx.coroutines.launch
import androidx.compose.material3.SheetValue
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarBlockAction
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.MyCalendarFab
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.MyCalendarScaffoldContent
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.detail.MyCalendarAppointmentDetailSheet
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.ownClient.OwnClientAction
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.ownClient.OwnClientSheet
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.ownClient.OwnClientSheetState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCalendarScreen(
    viewModel: MyCalendarViewModel,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val headerState by viewModel.calendarHeader.collectAsState()
    val calendarEvents by viewModel.calendarEvents.collectAsState()
    val selectedDay by viewModel.selectedDay.collectAsState()
    val slotDuration by viewModel.slotDuration.collectAsState()

    val isBlocking by viewModel.isBlocking.collectAsState()
    val defaultBlockedLocalDates by viewModel.defaultBlockedStartLocale.collectAsState()
    val blockedLocalDates by viewModel.selectedStartLocale.collectAsState()

    val selectedOwnClient by viewModel.selectedOwnClient.collectAsState()

    val isSaving by viewModel.isSaving.collectAsState()

    val settingsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val detailSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val blockSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showOwnClientSheet by rememberSaveable { mutableStateOf(false) }

    val ownClientSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newState ->
            when (newState) {
                SheetValue.Hidden -> false
                else -> true
            }
        }
    )

    val blockUiState = BlockUiState(
        isBlocking = isBlocking,
        defaultBlockedLocalDates = defaultBlockedLocalDates,
        blockedLocalDates = blockedLocalDates
    )

    if(settingsSheetState.isVisible) {
        MyCalendarSettingsSheet(
            sheetState = settingsSheetState,
            onClose = { scope.launch { settingsSheetState.hide() } }
        )
    }

    if(detailSheetState.isVisible) {
        MyCalendarAppointmentDetailSheet(
            sheetState = detailSheetState,
            onClose = { scope.launch { detailSheetState.hide() } }
        )
    }

    if (blockSheetState.isVisible) {
        BlockSlotsSheet(
            sheetState = blockSheetState,
            blockUiState = blockUiState,
            state = BlockSlotsSheetState(
                slotCount = blockedLocalDates.size - defaultBlockedLocalDates.size,
                selectedSlots = blockedLocalDates - defaultBlockedLocalDates,
                selectedDay = selectedDay,
                isSaving = isSaving
            ),
            onAction = { action ->
                when(action) {
                    is BlockSlotsAction.Confirm -> {
                        //viewModel.blockAppointments()
                    }
                    is BlockSlotsAction.Dismiss -> scope.launch { blockSheetState.hide() }
                }
            }
        )
    }

    if(showOwnClientSheet) {
        OwnClientSheet(
            sheetState = ownClientSheetState,
            state = OwnClientSheetState(
                isSaving = isSaving,
                selectedDay = selectedDay,
                selectedOwnClientSlot = selectedOwnClient,
                slotDuration = slotDuration
            ),
            onAction = { action ->
                when(action) {
                    is OwnClientAction.CreateOwnClient -> { viewModel.createOwnClientAppointment(action.request) }
                    is OwnClientAction.CreateLastMinute -> { viewModel.createLastMinute(action.request) }
                    is OwnClientAction.Close -> {
                        scope.launch {
                            ownClientSheetState.hide()
                            showOwnClientSheet = false
                        }
                    }
                }
            },
        )
    }

    Scaffold(
        floatingActionButton = {
            MyCalendarFab(
                calendarEvents = calendarEvents,
                isBlocking = isBlocking,
                onClick = {
                    scope.launch {
                        viewModel.setSelectedOwnClient(null)
                        ownClientSheetState.show()
                        showOwnClientSheet = true
                    }
                }
            )
        },
        bottomBar = {
            MyCalendarBlockAction(
                isEnabled = defaultBlockedLocalDates != blockedLocalDates,
                isBlocking = isBlocking,
                onCancel = { viewModel.resetSelectedLocalDates() },
                onBlockConfirm = { scope.launch { blockSheetState.show() } }
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            MyCalendarScaffoldContent(
                headerState = headerState,
                calendarEvents = calendarEvents,
                slotDuration = slotDuration,
                blockUiState = blockUiState,
                onAction = { action ->
                    when(action) {
                        is MyCalendarAction.Back -> { onBack() }
                        is MyCalendarAction.OnBlockToggle -> { viewModel.toggleBlocking() }
                        is MyCalendarAction.DayChanged -> {
                            viewModel.setDay(action.day)
                            if(isBlocking) viewModel.resetSelectedLocalDates()
                        }
                        is MyCalendarAction.DayRefresh -> scope.launch { viewModel.refreshCurrentDay() }
                        is MyCalendarAction.SlotDurationChanged -> { viewModel.setSlotDuration(action.value) }
                        is MyCalendarAction.SlotClick -> {
                            if(!action.slot.isBooked) {
                                viewModel.setSelectedOwnClient(action.slot)
                                scope.launch {
                                    ownClientSheetState.show()
                                    showOwnClientSheet = true
                                }
                            }
                        }
                    }
                },
            )
        }
    }
}