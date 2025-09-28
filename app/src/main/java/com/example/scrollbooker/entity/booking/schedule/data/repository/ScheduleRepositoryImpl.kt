package com.example.scrollbooker.entity.booking.schedule.data.repository
import com.example.scrollbooker.entity.auth.data.mappers.toDomain
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.booking.schedule.data.mappers.toDomain
import com.example.scrollbooker.entity.booking.schedule.data.mappers.toDto
import com.example.scrollbooker.entity.booking.schedule.data.remote.SchedulesApiService
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.entity.booking.schedule.domain.repository.ScheduleRepository
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val api: SchedulesApiService
): ScheduleRepository {
    override suspend fun getSchedules(userId: Int): List<Schedule> {
        return api.getSchedules(userId).map { it.toDomain() }
    }

    override suspend fun updateSchedules(schedules: List<Schedule>): List<Schedule> {
        return api.updateSchedules(schedules.toDto()).toDomain()
    }

}