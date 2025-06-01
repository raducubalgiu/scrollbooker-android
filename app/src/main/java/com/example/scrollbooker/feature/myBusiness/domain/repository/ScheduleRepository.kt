package com.example.scrollbooker.feature.myBusiness.domain.repository

import com.example.scrollbooker.feature.myBusiness.domain.model.Schedule

interface ScheduleRepository {
    suspend fun getSchedules(userId: Int): List<Schedule>
}