package com.example.scrollbooker.ui.myBusiness.myCalendar.components.header

data class MyCalendarHeaderActionsState(
    val isBlocking: Boolean,
    val slotDuration: String,
    val enableBack: Boolean,
    val enableNext: Boolean,
    val hasFreeSlots: Boolean
)

sealed interface MyCalendarHeaderActionsStateAction {
    data object HandlePreviousWeek: MyCalendarHeaderActionsStateAction
    data object HandleNextWeek: MyCalendarHeaderActionsStateAction
    data object OnBlockToggle: MyCalendarHeaderActionsStateAction
    data class OnSlotChange(val slotDuration: String?): MyCalendarHeaderActionsStateAction
}