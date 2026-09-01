package com.example.scrollbooker.entity.booking.availability.domain.useCase
import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.booking.availability.domain.model.CalendarEvents
import com.example.scrollbooker.entity.booking.availability.domain.repository.AvailabilityRepository
import javax.inject.Inject

class GetUserCalendarEventsUseCase @Inject constructor(
    private val repository: AvailabilityRepository
) {
    suspend operator fun invoke(
        businessId: Int,
        employeeId: Int?,
        startDate: String,
        endDate: String,
        slotDuration: Int
    ): Result<CalendarEvents>  {
        return runSuspendCatching {
            repository.getUserCalendarEvents(
                businessId = businessId,
                employeeId = employeeId,
                startDate = startDate,
                endDate = endDate,
                slotDuration = slotDuration
            )
        }
    }
}