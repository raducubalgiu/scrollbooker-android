package com.example.scrollbooker.entity.booking.availability.domain.useCase
import com.example.scrollbooker.entity.booking.availability.domain.repository.AvailabilityRepository
import javax.inject.Inject

class GetCalendarAvailableDaysUseCase @Inject constructor(
    private val repository: AvailabilityRepository
) {
    suspend operator fun invoke(userId: Int, startDate: String, endDate: String): List<String> {
        return repository.getUserCalendarAvailableDays(userId, startDate, endDate)
    }
}