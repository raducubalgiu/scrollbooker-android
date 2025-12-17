package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets

sealed interface MyCalendarSheet {
    data object Settings: MyCalendarSheet
    data object Detail: MyCalendarSheet
    data object Block: MyCalendarSheet
    data object OwnClient: MyCalendarSheet
}