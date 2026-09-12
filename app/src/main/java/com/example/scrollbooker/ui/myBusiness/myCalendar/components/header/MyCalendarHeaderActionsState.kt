package com.example.scrollbooker.ui.myBusiness.myCalendar.components.header

data class MyCalendarHeaderActionsState(
    val isBlocking: Boolean,
    val enableBack: Boolean,
    val enableNext: Boolean,
    val hasFreeSlots: Boolean,
    val hasEmployees: Boolean,
    val selectedEmployeeName: String?,
    val selectedEmployeeAvatar: String?,
    val ownFullName: String?,
    val ownAvatar: String?
)

sealed interface MyCalendarHeaderActionsStateAction {
    data object HandlePreviousWeek: MyCalendarHeaderActionsStateAction
    data object HandleNextWeek: MyCalendarHeaderActionsStateAction
    data object OnBlockToggle: MyCalendarHeaderActionsStateAction
    data object OpenEmployeeSheet: MyCalendarHeaderActionsStateAction
}