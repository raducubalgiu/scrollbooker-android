package com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.sheets
import com.example.scrollbooker.entity.booking.availability.domain.model.Slot
import com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.AddOwnClientViewModel

fun handleOwnClientSheetsAction(
    action: OwnClientSheetsAction,
    viewModel: AddOwnClientViewModel,
    onCloseSheet: () -> Unit,
    onSlotConfirmed: (Slot) -> Unit,
) {
    when (action) {
        OwnClientSheetsAction.Dismiss -> onCloseSheet()

        is OwnClientSheetsAction.ClientQueryChange -> viewModel.handleSearch(action.query)

        is OwnClientSheetsAction.ConfirmClient -> viewModel.selectClient(action.client)

        is OwnClientSheetsAction.SaveNewClient -> viewModel.createClient(action.fullname, action.phone)

        is OwnClientSheetsAction.ConfirmServices -> {
            viewModel.setLinkedItems(action.items)
            onCloseSheet()
        }

        is OwnClientSheetsAction.DayClick -> viewModel.selectCalendarDay(action.date)

        is OwnClientSheetsAction.ConfirmSlot -> onSlotConfirmed(action.slot)

        OwnClientSheetsAction.RefreshDaySlots -> viewModel.refreshDaySlots()
    }
}
