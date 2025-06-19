package com.example.scrollbooker.shared.schedules.data.repository
import com.example.scrollbooker.shared.schedules.data.mappers.toDomain
import com.example.scrollbooker.shared.schedules.data.mappers.toDto
import com.example.scrollbooker.shared.schedules.data.remote.SchedulesApiService
import com.example.scrollbooker.shared.schedules.domain.model.Schedule
import com.example.scrollbooker.shared.schedules.domain.repository.ScheduleRepository
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val api: SchedulesApiService
): ScheduleRepository {
    override suspend fun getSchedules(userId: Int): List<Schedule> {
        return api.getSchedules(userId).map { it.toDomain() }
    }

    override suspend fun updateSchedules(schedules: List<Schedule>): List<Schedule> {
        val dtoList = schedules.toDto()
        val response = api.updateSchedules(dtoList)
        return response.toDomain()
    }

}