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
import org.threeten.bp.LocalDate
import androidx.compose.material3.SheetValue
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarBlockAction
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.MyCalendarFab
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.MyCalendarScaffoldContent
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.ownClient.OwnClientSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCalendarScreen(
    viewModel: MyCalendarViewModel,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val headerState by viewModel.calendarHeader.collectAsState()
    val slotDuration by viewModel.slotDuration.collectAsState()

    val defaultBlockedLocalDates by viewModel.defaultBlockedStartLocale.collectAsState()
    val blockedLocalDates by viewModel.selectedStartLocale.collectAsState()

    val calendarEvents by viewModel.calendarEvents.collectAsState()
    val selectedDay by viewModel.selectedDay.collectAsState()
    val selectedOwnClient by viewModel.selectedOwnClient.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    var message by rememberSaveable { mutableStateOf("") }
    var isBlocking by rememberSaveable { mutableStateOf(false) }

    fun handleDayChange(day: LocalDate) { viewModel.setDay(day) }

    val blockSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val settingsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
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

    val blockSheetUIState = BlockSlotsSheetState(
        message = message,
        slotCount = blockedLocalDates.size - defaultBlockedLocalDates.size,
        selectedSlots = blockedLocalDates - defaultBlockedLocalDates,
        selectedDay = selectedDay,
        isSaving = isSaving
    )

    if (blockSheetState.isVisible) {
        BlockSlotsSheet(
            sheetState = blockSheetState,
            state = blockSheetUIState,
            onAction = { action ->
                when(action) {
                    is BlockSlotsAction.Confirm -> {
                        viewModel.blockAppointments(message)
                        isBlocking = false
                    }
                    is BlockSlotsAction.Dismiss -> scope.launch { blockSheetState.hide() }
                    is BlockSlotsAction.MessageChanged -> { message = action.value }
                }
            }
        )
    }

    if(settingsSheetState.isVisible) {
        MyCalendarSettingsSheet(
            sheetState = settingsSheetState,
            onDismiss = { scope.launch { settingsSheetState.hide() } }
        )
    }

    if(showOwnClientSheet) {
        OwnClientSheet(
            sheetState = ownClientSheetState,
            isSaving = isSaving,
            selectedOwnClientSlot = selectedOwnClient,
            slotDuration = slotDuration,
            onCreateOwnClient = { viewModel.createOwnClientAppointment(it) },
            onCreateLastMinute = { viewModel.createLastMinute(it) },
            onClose = {
                scope.launch { ownClientSheetState.hide() }
                showOwnClientSheet = false
            },
        )
    }

    Scaffold(
        bottomBar = { if(isBlocking) {
            MyCalendarBlockAction(
                isEnabled = defaultBlockedLocalDates != blockedLocalDates,
                onCancel = {
                    viewModel.resetSelectedLocalDates()
                    isBlocking = false
                },
                onBlockConfirm = { scope.launch { blockSheetState.show() } }
            )
        }
        },
        floatingActionButton = {
            MyCalendarFab(
                calendarEvents = calendarEvents,
                onClick = {}
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            MyCalendarScaffoldContent(
                headerState = headerState,
                calendarEvents = calendarEvents,
                slotDuration = slotDuration,
                isBlocking = isBlocking,
                onDayChange = { handleDayChange(it) },
                onSlotDurationChange = { viewModel.setSlotDuration(it) },
                onOpenOwnClientSheet = {
                    viewModel.setSelectedOwnClient(it)
                    scope.launch {
                        ownClientSheetState.show()
                        showOwnClientSheet = true
                    }
                },
                defaultBlockedLocalDates = defaultBlockedLocalDates,
                blockedLocalDates = blockedLocalDates,
                onBack = onBack
            )
        }
    }
}