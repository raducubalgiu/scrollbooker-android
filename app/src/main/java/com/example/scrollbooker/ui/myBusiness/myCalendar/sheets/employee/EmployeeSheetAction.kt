package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.employee

sealed interface EmployeeSheetAction {
    data class Select(val employeeId: Int): EmployeeSheetAction
    data object Close: EmployeeSheetAction
}
