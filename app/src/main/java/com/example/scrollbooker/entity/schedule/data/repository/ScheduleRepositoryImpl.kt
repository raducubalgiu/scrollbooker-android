package com.example.scrollbooker.entity.schedule.data.repository
import com.example.scrollbooker.entity.schedule.data.mappers.toDomain
import com.example.scrollbooker.entity.schedule.data.mappers.toDto
import com.example.scrollbooker.entity.schedule.data.remote.SchedulesApiService
import com.example.scrollbooker.entity.schedule.domain.model.Schedule
import com.example.scrollbooker.entity.schedule.domain.repository.ScheduleRepository
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