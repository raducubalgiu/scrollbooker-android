package com.example.scrollbooker.entity.schedule.domain.useCase
import com.example.scrollbooker.entity.schedule.domain.model.Schedule
import com.example.scrollbooker.entity.schedule.domain.repository.ScheduleRepository
import javax.inject.Inject

class UpdateSchedulesUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
) {
    suspend operator fun invoke(schedules: List<Schedule>): Result<List<Schedule>> = runCatching {
        scheduleRepository.updateSchedules(schedules)
    }
}