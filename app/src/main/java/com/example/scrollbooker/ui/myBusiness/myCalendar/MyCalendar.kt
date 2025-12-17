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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.block.BlockSlotsAction
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.block.BlockSlotsSheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsSlot
import com.example.scrollbooker.entity.booking.calendar.domain.model.isFreeSlot
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarBlockAction
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.MyCalendarFab
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.MyCalendarScaffoldContent
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.MyCalendarSheet
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.MyCalendarSheetController
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.MyCalendarSheets
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.ownClient.OwnClientAction
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.ownClient.OwnClientSheetState
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.rememberMyCalendarSheetController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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

    val blockUiState = BlockUiState(
        isBlocking = isBlocking,
        defaultBlockedLocalDates = defaultBlockedLocalDates,
        blockedLocalDates = blockedLocalDates
    )

    val ownClientState = OwnClientSheetState(
        isSaving = isSaving,
        selectedDay = selectedDay,
        selectedOwnClientSlot = selectedOwnClient,
        slotDuration = slotDuration
    )

    val blockState = BlockSlotsSheetState(
        slotCount = blockedLocalDates.size - defaultBlockedLocalDates.size,
        selectedSlots = blockedLocalDates - defaultBlockedLocalDates,
        selectedDay = selectedDay,
        isSaving = isSaving
    )

    var dismissEnabledGate by remember { mutableStateOf(true) }
    var allowHideGate by remember { mutableStateOf(false) }

    val dismissEnabledState = rememberUpdatedState(dismissEnabledGate)
    val allowHideState = rememberUpdatedState(allowHideGate)

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            if(newValue == SheetValue.Hidden) {
                dismissEnabledState.value || allowHideState.value
            } else true
        }
    )

    val sheets = rememberMyCalendarSheetController(
        sheetState = sheetState,
        onSheetCleared = { viewModel.setSelectedOwnClient(null) },
        onDismissEnabledChanged = { dismissEnabledGate = it },
        onAllowHideChanged = { allowHideGate = it }
    )

    MyCalendarSheets(
        sheetState = sheetState,
        controller = sheets,
        ownClientState = ownClientState,
        blockState = blockState,
        onOwnClientAction = { action ->
            when(action) {
                is OwnClientAction.CreateOwnClient -> viewModel.createOwnClientAppointment(action.request)
                is OwnClientAction.CreateLastMinute -> viewModel.createLastMinute(action.request)
                OwnClientAction.Close -> sheets.closeOwnClient()
            }
        },
        onBlockAction = { action ->
            when(action) {
                is BlockSlotsAction.Confirm -> {
                    viewModel.blockAppointments(action.message)
                    viewModel.resetSelectedLocalDates()
                }
                BlockSlotsAction.Dismiss -> sheets.close()
            }
        }
    )

    Scaffold(
        floatingActionButton = {
            MyCalendarFab(
                calendarEvents = calendarEvents,
                isBlocking = isBlocking,
                onClick = {
                    viewModel.setSelectedOwnClient(null)
                    sheets.open(MyCalendarSheet.OwnClient)
                }
            )
        },
        bottomBar = {
            MyCalendarBlockAction(
                isEnabled = defaultBlockedLocalDates != blockedLocalDates,
                isBlocking = isBlocking,
                onCancel = { viewModel.resetSelectedLocalDates() },
                onBlockConfirm = { sheets.open(MyCalendarSheet.Block) }
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
                onAction = { action -> handleMyCalendarAction(action, viewModel, sheets, isBlocking, onBack, scope) },
            )
        }
    }
}

private fun handleMyCalendarAction(
    action: MyCalendarAction,
    viewModel: MyCalendarViewModel,
    sheets: MyCalendarSheetController,
    isBlocking: Boolean,
    onBack: () -> Unit,
    scope: CoroutineScope
) {
    when (action) {
        is MyCalendarAction.DayChanged -> {
            viewModel.setDay(action.day)
            if (isBlocking) viewModel.resetSelectedLocalDates()
        }

        is MyCalendarAction.SlotDurationChanged ->
            viewModel.setSlotDuration(action.value)

        is MyCalendarAction.SlotClick ->
            handleSlotClick(action.slot, viewModel, sheets, isBlocking)

        MyCalendarAction.Back -> onBack()

        MyCalendarAction.Settings -> sheets.open(MyCalendarSheet.Settings)

        MyCalendarAction.OnBlockToggle -> { viewModel.toggleBlocking() }

        MyCalendarAction.DayRefresh -> scope.launch { viewModel.refreshCurrentDay() }

    }
}

private fun handleSlotClick(
    slot: CalendarEventsSlot,
    viewModel: MyCalendarViewModel,
    sheets: MyCalendarSheetController,
    isBlocking: Boolean,
) {
    when {
        slot.isBooked -> sheets.open(MyCalendarSheet.Detail)

        isBlocking && slot.isFreeSlot() -> viewModel.setBlockDate(slot.startDateLocale!!)

        slot.isFreeSlot() -> {
            viewModel.setSelectedOwnClient(slot)
            sheets.open(MyCalendarSheet.OwnClient)
        }
    }
}
