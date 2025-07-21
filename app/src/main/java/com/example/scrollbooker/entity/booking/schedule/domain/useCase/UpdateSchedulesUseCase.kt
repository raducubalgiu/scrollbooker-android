package com.example.scrollbooker.entity.booking.schedule.domain.useCase
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.entity.booking.schedule.domain.repository.ScheduleRepository
import javax.inject.Inject

class UpdateSchedulesUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
) {
    suspend operator fun invoke(schedules: List<Schedule>): Result<AuthState> = runCatching {
        scheduleRepository.updateSchedules(schedules)
    }
}