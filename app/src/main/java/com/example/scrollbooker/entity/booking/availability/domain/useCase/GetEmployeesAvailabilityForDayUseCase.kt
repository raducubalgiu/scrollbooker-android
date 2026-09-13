package com.example.scrollbooker.entity.booking.availability.domain.useCase

import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.booking.availability.domain.model.EmployeeAvailability
import com.example.scrollbooker.entity.booking.availability.domain.repository.AvailabilityRepository
import javax.inject.Inject

class GetEmployeesAvailabilityForDayUseCase @Inject constructor(
    private val repository: AvailabilityRepository
) {
    suspend operator fun invoke(day: String, slotDuration: Int): Result<List<EmployeeAvailability>> =
        runSuspendCatching {
            repository.getEmployeesAvailabilityForDay(day = day, slotDuration = slotDuration)
        }
}
