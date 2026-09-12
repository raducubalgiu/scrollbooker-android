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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.scrollbooker.core.snackbar.CustomSnackBar
import com.example.scrollbooker.core.snackbar.rememberSnackBarController
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.block.BlockSlotsAction
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.block.BlockSlotsSheetState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarBlockAction
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.MyCalendarFab
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.MyCalendarScaffoldContent
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.MyCalendarSheet
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.MyCalendarSheets
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.employee.EmployeeSheetAction
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.rememberMyCalendarSheetController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCalendarScreen(
    viewModel: MyCalendarViewModel,
    profileNavigate: ProfileNavigator,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val headerState by viewModel.calendarHeader.collectAsState()
    val calendarEvents by viewModel.calendarEvents.collectAsStateWithLifecycle()
    val daySchedule by viewModel.daySchedule.collectAsStateWithLifecycle()
    val selectedDay by viewModel.selectedDay.collectAsStateWithLifecycle()
    val slotDuration by viewModel.slotDuration.collectAsStateWithLifecycle()
    val businessDayWindow by viewModel.businessDayWindow.collectAsStateWithLifecycle()

    val isBlocking by viewModel.isBlocking.collectAsStateWithLifecycle()
    val defaultBlockedLocalDates by viewModel.defaultBlockedStartLocale.collectAsStateWithLifecycle()
    val blockedLocalDates by viewModel.selectedStartLocale.collectAsStateWithLifecycle()

    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()
    val actionSucceededTick by viewModel.actionSucceededTick.collectAsStateWithLifecycle()
    val isRefreshingCurrentDay by viewModel.isRefreshingCurrentDay.collectAsStateWithLifecycle()

    val employees by viewModel.employees.collectAsStateWithLifecycle()
    val selectedEmployeeId by viewModel.selectedEmployeeId.collectAsStateWithLifecycle()
    val selectedEmployee by viewModel.selectedEmployee.collectAsStateWithLifecycle()
    val hasEmployees = (employees as? FeatureState.Success)?.data?.isNotEmpty() == true

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

    val blockState = BlockSlotsSheetState(
        slotCount = blockedLocalDates.size - defaultBlockedLocalDates.size,
        selectedSlots = blockedLocalDates - defaultBlockedLocalDates,
        selectedDay = selectedDay,
        isSaving = isSaving,
        successTick = actionSucceededTick
    )

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val sheets = rememberMyCalendarSheetController(sheetState = sheetState)

    MyCalendarSheets(
        sheetState = sheetState,
        controller = sheets,
        blockState = blockState,
        onBlockAction = { action ->
            when(action) {
                is BlockSlotsAction.Confirm -> viewModel.blockAppointments(action.message)
                BlockSlotsAction.Dismiss -> sheets.close()
            }
        },
        employees = employees,
        selectedEmployeeId = selectedEmployeeId,
        onEmployeeAction = { action ->
            when(action) {
                is EmployeeSheetAction.Select -> {
                    viewModel.selectEmployee(action.employeeId)
                    sheets.close()
                }
                EmployeeSheetAction.Close -> sheets.close()
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
                        profileNavigate.toAddOwnClientAppointment()
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
                    businessDayWindow = businessDayWindow,
                    blockUiState = blockUiState,
                    isRefreshing = isRefreshingCurrentDay,
                    hasEmployees = hasEmployees,
                    selectedEmployee = selectedEmployee,
                    onAction = { action ->
                        handleMyCalendarAction(
                            action = action,
                            viewModel = viewModel,
                            sheets = sheets,
                            isBlocking = isBlocking,
                            onBack = onBack,
                            onNavigateToAddOwnClient = { profileNavigate.toAddOwnClientAppointment() },
                            onNavigateToAppointmentDetails = { profileNavigate.toAppointmentDetails(it) },
                            scope = scope
                        )
                    },
                )
            }
        }

        CustomSnackBar(hostState = snackbarHostState)
    }
}
