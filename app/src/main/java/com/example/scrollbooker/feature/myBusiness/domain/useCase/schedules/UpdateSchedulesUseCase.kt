package com.example.scrollbooker.feature.myBusiness.domain.useCase.schedules
import com.example.scrollbooker.feature.myBusiness.domain.model.Schedule
import com.example.scrollbooker.feature.myBusiness.domain.repository.ScheduleRepository
import javax.inject.Inject

class UpdateSchedulesUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
) {
    suspend operator fun invoke(schedules: List<Schedule>): Result<List<Schedule>> = runCatching {
        scheduleRepository.updateSchedules(schedules)
    }
}