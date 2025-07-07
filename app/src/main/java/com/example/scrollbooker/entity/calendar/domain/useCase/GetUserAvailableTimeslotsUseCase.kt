package com.example.scrollbooker.entity.calendar.domain.useCase
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.calendar.domain.model.AvailableDay
import com.example.scrollbooker.entity.calendar.domain.repository.CalendarRepository
import timber.log.Timber
import javax.inject.Inject

class GetUserAvailableTimeslotsUseCase @Inject constructor(
    private val repository: CalendarRepository
) {
    suspend operator fun invoke(day: String, userId: Int, slotDuration: Int): FeatureState<AvailableDay> {
        return try {
            val response = repository.getUserAvailableTimeSlots(day, userId, slotDuration)
            return FeatureState.Success(response)
        } catch (e: Exception) {
            Timber.tag("Calendar").e("ERROR: on Fetching User Available Timeslots $e")
            FeatureState.Error()
        }
    }
}