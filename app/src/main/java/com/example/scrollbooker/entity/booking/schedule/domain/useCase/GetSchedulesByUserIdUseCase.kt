package com.example.scrollbooker.entity.booking.schedule.domain.useCase
import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.entity.booking.schedule.domain.repository.ScheduleRepository
import javax.inject.Inject

class GetSchedulesByUserIdUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {
    suspend operator fun invoke(userId: Int): Result<List<Schedule>> {
        return runSuspendCatching {
            scheduleRepository.getSchedules(userId) }
        }
}