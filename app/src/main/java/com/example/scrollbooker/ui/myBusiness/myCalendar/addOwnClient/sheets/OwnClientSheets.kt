package com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.sheets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.sheets.createClient.AddBusinessClientSheet
import com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.sheets.selectClient.BusinessClientSelectSheet
import com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.sheets.selectDateTime.DateTimePickerSheet
import com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.sheets.selectServices.ServicesSelectSheet
import com.example.scrollbooker.ui.theme.Background

// Single host for every AddOwnClientScreen sheet, mirroring PostSheets: SelectClient/AddClient/
// DateTime are bare content sharing one ModalBottomSheet + SheetState; Services (ServicesSelectSheet)
// wraps its own reused Sheet (same component CreatePostScreen's sheets use) with its own
// styling/nested ProductDetailSheet, so it keeps its own SheetState, but still lives here
// alongside the others.
//
// Takes one state bundle + one action callback (see OwnClientSheetsState/Action and
// handleOwnClientSheetsAction) instead of ~20 individual props, matching the state/action split
// already used for MyCalendarHeaderActions/BlockSlotsSheet/EmployeeSelectSheet.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnClientSheets(
    state: OwnClientSheetsState,
    onAction: (OwnClientSheetsAction) -> Unit,
) {
    if (
        state.currentSheet == AddOwnClientSheet.SelectClient ||
        state.currentSheet == AddOwnClientSheet.AddClient ||
        state.currentSheet == AddOwnClientSheet.DateTime
    ) {
        ModalBottomSheet(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            sheetState = state.sheetState,
            onDismissRequest = { onAction(OwnClientSheetsAction.Dismiss) },
            containerColor = Background,
            dragHandle = {}
        ) {
            when (state.currentSheet) {
                AddOwnClientSheet.SelectClient -> BusinessClientSelectSheet(
                    query = state.clientQuery,
                    searchState = state.clientSearchState,
                    selectedClient = state.selectedClient,
                    onQueryChange = { onAction(OwnClientSheetsAction.ClientQueryChange(it)) },
                    onConfirm = { onAction(OwnClientSheetsAction.ConfirmClient(it)) },
                    onDismiss = { onAction(OwnClientSheetsAction.Dismiss) }
                )

                AddOwnClientSheet.AddClient -> AddBusinessClientSheet(
                    isSaving = state.isCreatingClient,
                    onSave = { fullname, phone -> onAction(OwnClientSheetsAction.SaveNewClient(fullname, phone)) },
                    onDismiss = { onAction(OwnClientSheetsAction.Dismiss) }
                )

                AddOwnClientSheet.DateTime -> DateTimePickerSheet(
                    sheetState = state.sheetState,
                    calendarHeaderState = state.calendarHeaderState,
                    selectedDay = state.selectedCalendarDay,
                    daySlots = state.daySlots,
                    startOnSlotsStep = state.startOnSlotsStep,
                    initialPendingSlotUtc = state.initialPendingSlotUtc,
                    onDayClick = { onAction(OwnClientSheetsAction.DayClick(it)) },
                    onConfirm = { onAction(OwnClientSheetsAction.ConfirmSlot(it)) },
                    onDismiss = { onAction(OwnClientSheetsAction.Dismiss) }
                )

                else -> Unit
            }
        }
    }

    if (state.currentSheet == AddOwnClientSheet.Services) {
        ServicesSelectSheet(
            sheetState = state.servicesSheetState,
            userProducts = state.userProducts,
            linkedItems = state.linkedItems,
            onConfirm = { onAction(OwnClientSheetsAction.ConfirmServices(it)) },
            onClose = { onAction(OwnClientSheetsAction.Dismiss) }
        )
    }
}
