package com.example.scrollbooker.entity.booking.availability.data.repository
import com.example.scrollbooker.entity.booking.availability.data.mappers.toDomain
import com.example.scrollbooker.entity.booking.availability.data.remote.AvailabilityApiService
import com.example.scrollbooker.entity.booking.availability.domain.model.AvailableDay
import com.example.scrollbooker.entity.booking.availability.domain.model.CalendarEvents
import com.example.scrollbooker.entity.booking.availability.domain.repository.AvailabilityRepository
import javax.inject.Inject

class AvailabilityRepositoryImpl @Inject constructor(
    private val apiService: AvailabilityApiService
): AvailabilityRepository {
    override suspend fun getUserCalendarAvailableDays(
        userId: Int,
        startDate: String,
        endDate: String
    ): List<String> {
        return apiService.getUserCalendarAvailableDays(userId, startDate, endDate)
    }

    override suspend fun getUserAvailableTimeSlots(
        day: String,
        userId: Int,
        slotDuration: Int
    ): AvailableDay {
        return apiService.getUserAvailableTimeslots(day, userId, slotDuration).toDomain()
    }

    override suspend fun getUserCalendarEvents(
        startDate: String,
        endDate: String,
        userId: Int,
        slotDuration: Int
    ): CalendarEvents {
        return apiService.getUserCalendarEvents(startDate, endDate, userId, slotDuration).toDomain()
    }
}