package com.example.scrollbooker.feature.schedules.domain.useCase
import com.example.scrollbooker.feature.schedules.domain.model.Schedule
import com.example.scrollbooker.feature.schedules.domain.repository.ScheduleRepository
import javax.inject.Inject

class UpdateSchedulesUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
) {
    suspend operator fun invoke(schedules: List<Schedule>): Result<List<Schedule>> = runCatching {
        scheduleRepository.updateSchedules(schedules)
    }
}