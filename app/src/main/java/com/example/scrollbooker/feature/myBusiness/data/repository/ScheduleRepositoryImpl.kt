package com.example.scrollbooker.feature.myBusiness.data.repository

import com.example.scrollbooker.feature.myBusiness.data.mappers.toDomain
import com.example.scrollbooker.feature.myBusiness.data.remote.schedules.SchedulesApiService
import com.example.scrollbooker.feature.myBusiness.domain.model.Schedule
import com.example.scrollbooker.feature.myBusiness.domain.repository.ScheduleRepository
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val api: SchedulesApiService
): ScheduleRepository {
    override suspend fun getSchedules(userId: Int): List<Schedule> {
        return api.getSchedules(userId).map { it.toDomain() }
    }

}