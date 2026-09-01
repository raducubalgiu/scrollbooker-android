package com.example.scrollbooker.ui.myBusiness.myCalendar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.scrollbooker.core.snackbar.CustomSnackBar
import com.example.scrollbooker.core.snackbar.rememberSnackBarController
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.block.BlockSlotsAction
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.block.BlockSlotsSheetState
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.duration.DurationSheetAction
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarBlockAction
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.MyCalendarFab
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.MyCalendarScaffoldContent
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.MyCalendarSheet
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.MyCalendarSheets
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.ownClient.OwnClientAction
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.ownClient.OwnClientSheetState
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.rememberMyCalendarSheetController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCalendarScreen(
    viewModel: MyCalendarViewModel,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val headerState by viewModel.calendarHeader.collectAsState()
    val calendarEvents by viewModel.calendarEvents.collectAsStateWithLifecycle()
    val daySchedule by viewModel.daySchedule.collectAsStateWithLifecycle()
    val selectedDay by viewModel.selectedDay.collectAsStateWithLifecycle()
    val slotDuration by viewModel.slotDuration.collectAsStateWithLifecycle()

    val isBlocking by viewModel.isBlocking.collectAsStateWithLifecycle()
    val defaultBlockedLocalDates by viewModel.defaultBlockedStartLocale.collectAsStateWithLifecycle()
    val blockedLocalDates by viewModel.selectedStartLocale.collectAsStateWithLifecycle()

    val selectedOwnClient by viewModel.selectedOwnClient.collectAsStateWithLifecycle()
    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()
    val actionSucceededTick by viewModel.actionSucceededTick.collectAsStateWithLifecycle()
    val userId by viewModel.userId.collectAsStateWithLifecycle()
    val isRefreshingCurrentDay by viewModel.isRefreshingCurrentDay.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val snackBarController = rememberSnackBarController(snackbarHostState)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event -> snackBarController.show(event) }
    }

    val blockUiState = BlockUiState(
        isBlocking = isBlocking,
        defaultBlockedLocalDates = defaultBlockedLocalDates,
        blockedLocalDates = blockedLocalDates
    )

    val ownClientState = OwnClientSheetState(
        isSaving = isSaving,
        userId = userId,
        selectedDay = selectedDay,
        selectedOwnClientSlot = selectedOwnClient,
        slotDuration = slotDuration,
        successTick = actionSucceededTick
    )

    val blockState = BlockSlotsSheetState(
        slotCount = blockedLocalDates.size - defaultBlockedLocalDates.size,
        selectedSlots = blockedLocalDates - defaultBlockedLocalDates,
        selectedDay = selectedDay,
        isSaving = isSaving,
        successTick = actionSucceededTick
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
                is BlockSlotsAction.Confirm -> viewModel.blockAppointments(action.message)
                BlockSlotsAction.Dismiss -> sheets.close()
            }
        },
        selectedDuration = slotDuration.toString(),
        onDurationAction = { action ->
            when(action) {
                is DurationSheetAction.Select -> {
                    viewModel.setSlotDuration(action.value)
                    sheets.close()
                }
                DurationSheetAction.Close -> sheets.close()
            }
        }
    )

    Box(Modifier.fillMaxSize()) {
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
                    daySchedule = daySchedule,
                    slotDuration = slotDuration,
                    blockUiState = blockUiState,
                    isRefreshing = isRefreshingCurrentDay,
                    onAction = { action -> handleMyCalendarAction(action, viewModel, sheets, isBlocking, onBack, scope) },
                )
            }
        }

        CustomSnackBar(hostState = snackbarHostState)
    }
}
