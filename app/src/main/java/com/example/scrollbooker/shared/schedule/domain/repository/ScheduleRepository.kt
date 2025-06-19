package com.example.scrollbooker.shared.schedule.domain.repository

import com.example.scrollbooker.shared.schedule.domain.model.Schedule


interface ScheduleRepository {
    suspend fun getSchedules(userId: Int): List<Schedule>
    suspend fun updateSchedules(schedules: List<Schedule>): List<Schedule>
}