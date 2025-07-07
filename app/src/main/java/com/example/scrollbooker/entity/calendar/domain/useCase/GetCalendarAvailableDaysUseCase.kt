package com.example.scrollbooker.entity.calendar.domain.useCase
import com.example.scrollbooker.entity.calendar.domain.repository.CalendarRepository
import javax.inject.Inject

class GetCalendarAvailableDaysUseCase @Inject constructor(
    private val repository: CalendarRepository
) {
    suspend operator fun invoke(userId: Int, startDate: String, endDate: String): List<String> {
        return repository.getUserCalendarAvailableDays(userId, startDate, endDate)
    }
}