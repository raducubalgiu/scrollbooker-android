package com.example.scrollbooker.entity.booking.availability.domain.useCase
import com.example.scrollbooker.entity.booking.availability.domain.model.AvailableDay
import com.example.scrollbooker.entity.booking.availability.domain.repository.AvailabilityRepository
import javax.inject.Inject

class GetUserAvailableTimeslotsUseCase @Inject constructor(
    private val repository: AvailabilityRepository
) {
    suspend operator fun invoke(
        businessId: Int,
        employeeId: Int?,
        slotDuration: Int,
        day: String,
    ): AvailableDay {
        return repository.getUserAvailableTimeSlots(
            businessId = businessId,
            employeeId = employeeId,
            slotDuration = slotDuration,
            day = day
        )
    }
}