package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets

sealed interface MyCalendarSheet {
    data object Block: MyCalendarSheet
    data object Employee: MyCalendarSheet
}