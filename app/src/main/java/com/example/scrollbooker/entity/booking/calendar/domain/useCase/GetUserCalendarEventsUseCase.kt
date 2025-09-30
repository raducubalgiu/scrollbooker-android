package com.example.scrollbooker.entity.booking.calendar.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEvents
import com.example.scrollbooker.entity.booking.calendar.domain.repository.CalendarRepository
import timber.log.Timber
import javax.inject.Inject

class GetUserCalendarEventsUseCase @Inject constructor(
    private val repository: CalendarRepository
) {
    suspend operator fun invoke(
        startDate: String,
        endDate: String,
        userId: Int,
        slotDuration: Int
    ): FeatureState<CalendarEvents> {
        return try {
            val response = repository.getUserCalendarEvents(startDate, endDate, userId, slotDuration)
            return FeatureState.Success(response)
        } catch (e: Exception) {
            Timber.tag("Calendar").e("ERROR: on Fetching User Calendar Events $e")
            FeatureState.Error()
        }
    }
}