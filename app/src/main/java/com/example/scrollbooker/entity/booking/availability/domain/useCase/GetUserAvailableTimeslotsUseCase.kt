package com.example.scrollbooker.entity.booking.availability.domain.useCase
import com.example.scrollbooker.entity.booking.availability.domain.model.AvailableDay
import com.example.scrollbooker.entity.booking.availability.domain.repository.AvailabilityRepository
import javax.inject.Inject

class GetUserAvailableTimeslotsUseCase @Inject constructor(
    private val repository: AvailabilityRepository
) {
    suspend operator fun invoke(day: String, userId: Int, slotDuration: Int): AvailableDay {
        return repository.getUserAvailableTimeSlots(day, userId, slotDuration)
    }
}