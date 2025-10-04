package com.example.scrollbooker.ui.myBusiness.myCalendar.components

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Immutable
import org.threeten.bp.LocalDate

@Immutable
data class MyCalendarHeaderState(
    val weekPagerState: PagerState,
    val selectedTabIndex: Int,
    val period: String,
    val slotDuration: Int,
    val isBlocking: Boolean,
    val enableBack: Boolean,
    val enableNext: Boolean,
    val availableDays: List<LocalDate>,
    val calendarDays:  List<LocalDate>,
)

sealed interface MyCalendarHeaderStateAction {
    data object OnIsBlocking: MyCalendarHeaderStateAction
    data object HandlePreviousWeek: MyCalendarHeaderStateAction
    data object HandleNextWeek: MyCalendarHeaderStateAction
    data class OnChangeTab(val date: LocalDate, val index: Int): MyCalendarHeaderStateAction
    data class OnSlotChange(val slotDuration: String?): MyCalendarHeaderStateAction
}