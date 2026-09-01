package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.duration

sealed interface DurationSheetAction {
    data class Select(val value: String): DurationSheetAction
    data object Close: DurationSheetAction
}
