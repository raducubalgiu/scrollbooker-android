package com.example.scrollbooker.components.customized.calendar

import com.example.scrollbooker.core.util.FeatureState
import kotlinx.coroutines.flow.StateFlow

interface CalendarViewModelContract {
    val calendarHeader: StateFlow<FeatureState<CalendarHeaderState>>
}
