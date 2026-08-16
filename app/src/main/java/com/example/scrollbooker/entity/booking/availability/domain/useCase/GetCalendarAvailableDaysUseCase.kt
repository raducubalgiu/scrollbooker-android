package com.example.scrollbooker.entity.booking.availability.domain.useCase
import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.booking.availability.domain.repository.AvailabilityRepository
import javax.inject.Inject

class GetCalendarAvailableDaysUseCase @Inject constructor(
    private val repository: AvailabilityRepository
) {
    suspend operator fun invoke(
        businessId: Int,
        employeeId: Int?,
        startDate: String,
        endDate: String,
        slotDuration: Int,
    ): Result<List<String>> {
        return runSuspendCatching {
            repository.getUserCalendarAvailableDays(
                businessId = businessId,
                employeeId = employeeId,
                slotDuration = slotDuration,
                startDate = startDate,
                endDate = endDate
            )
        }
    }
}