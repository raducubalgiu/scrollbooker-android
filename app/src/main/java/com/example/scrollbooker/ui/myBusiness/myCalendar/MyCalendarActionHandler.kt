package com.example.scrollbooker.ui.myBusiness.myCalendar

import com.example.scrollbooker.entity.booking.availability.domain.model.CalendarEventsSlot
import com.example.scrollbooker.entity.booking.availability.domain.model.isFreeSlot
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.MyCalendarSheet
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.MyCalendarSheetController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun handleMyCalendarAction(
    action: MyCalendarAction,
    viewModel: MyCalendarViewModel,
    sheets: MyCalendarSheetController,
    isBlocking: Boolean,
    onBack: () -> Unit,
    onNavigateToAddOwnClient: () -> Unit,
    onNavigateToAppointmentDetails: (Int) -> Unit,
    onNavigateToSettings: () -> Unit,
    scope: CoroutineScope
) {
    when (action) {
        is MyCalendarAction.DayChanged -> {
            viewModel.setDay(action.day)
            if (isBlocking) viewModel.resetSelectedLocalDates()
        }

        is MyCalendarAction.SlotClick ->
            handleSlotClick(action.slot, viewModel, isBlocking, onNavigateToAddOwnClient, onNavigateToAppointmentDetails)

        MyCalendarAction.Back -> onBack()

        MyCalendarAction.Settings -> onNavigateToSettings()

        MyCalendarAction.OnBlockToggle -> viewModel.toggleBlocking()

        MyCalendarAction.OpenEmployeeSheet -> {
            viewModel.loadEmployeesAvailability()
            sheets.open(MyCalendarSheet.Employee)
        }

        MyCalendarAction.DayRefresh -> scope.launch { viewModel.refreshCurrentDay() }
    }
}

private fun handleSlotClick(
    slot: CalendarEventsSlot,
    viewModel: MyCalendarViewModel,
    isBlocking: Boolean,
    onNavigateToAddOwnClient: () -> Unit,
    onNavigateToAppointmentDetails: (Int) -> Unit,
) {
    when {
        slot.isBooked -> slot.id?.let(onNavigateToAppointmentDetails)

        isBlocking && slot.isFreeSlot() -> viewModel.setBlockDate(slot.startDateLocale!!)

        slot.isFreeSlot() -> {
            viewModel.setSelectedOwnClient(slot)
            onNavigateToAddOwnClient()
        }
    }
}
