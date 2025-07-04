package com.example.scrollbooker.entity.schedule.domain.repository

import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.schedule.domain.model.Schedule


interface ScheduleRepository {
    suspend fun getSchedules(userId: Int): List<Schedule>
    suspend fun updateSchedules(schedules: List<Schedule>): AuthState
}