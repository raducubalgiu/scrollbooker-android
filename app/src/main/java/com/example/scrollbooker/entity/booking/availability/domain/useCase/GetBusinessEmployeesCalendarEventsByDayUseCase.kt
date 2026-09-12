package com.example.scrollbooker.entity.booking.availability.domain.useCase
import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.booking.availability.domain.model.CalendarEventsBusinessDay
import com.example.scrollbooker.entity.booking.availability.domain.repository.AvailabilityRepository
import javax.inject.Inject

class GetBusinessEmployeesCalendarEventsByDayUseCase @Inject constructor(
    private val repository: AvailabilityRepository
) {
    suspend operator fun invoke(
        day: String,
        slotDuration: Int
    ): Result<CalendarEventsBusinessDay> {
        return runSuspendCatching {
            repository.getBusinessEmployeesCalendarEventsByDay(
                day = day,
                slotDuration = slotDuration
            )
        }
    }
}
