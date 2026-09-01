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
    scope: CoroutineScope
) {
    when (action) {
        is MyCalendarAction.DayChanged -> {
            viewModel.setDay(action.day)
            if (isBlocking) viewModel.resetSelectedLocalDates()
        }

        MyCalendarAction.OpenDurationSheet -> sheets.open(MyCalendarSheet.Duration)

        is MyCalendarAction.SlotClick ->
            handleSlotClick(action.slot, viewModel, sheets, isBlocking)

        MyCalendarAction.Back -> onBack()

        MyCalendarAction.Settings -> sheets.open(MyCalendarSheet.Settings)

        MyCalendarAction.OnBlockToggle -> viewModel.toggleBlocking()

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
