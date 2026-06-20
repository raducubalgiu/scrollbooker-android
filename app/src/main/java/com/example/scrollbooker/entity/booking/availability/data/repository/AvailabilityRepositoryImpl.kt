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
        businessId: Int,
        employeeId: Int?,
        startDate: String,
        endDate: String
    ): List<String> {
        return apiService.getUserCalendarAvailableDays(
            businessId = businessId,
            employeeId = employeeId,
            startDate = startDate,
            endDate = endDate
        )
    }

    override suspend fun getUserAvailableTimeSlots(
        businessId: Int,
        employeeId: Int?,
        slotDuration: Int,
        day: String,
    ): AvailableDay {
        return apiService.getUserAvailableTimeslots(
            businessId = businessId,
            employeeId = employeeId,
            slotDuration = slotDuration,
            day = day
        ).toDomain()
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