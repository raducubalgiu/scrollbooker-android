package com.example.scrollbooker.ui.myBusiness.myCalendar
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsSlot
import org.threeten.bp.LocalDate

sealed interface MyCalendarAction {
    data object Back: MyCalendarAction
    data class DayChanged(val day: LocalDate): MyCalendarAction
    data object DayRefresh: MyCalendarAction
    data class SlotDurationChanged(val value: String?): MyCalendarAction
    data class SlotClick(val slot: CalendarEventsSlot): MyCalendarAction
}