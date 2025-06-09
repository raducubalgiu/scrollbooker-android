package com.example.scrollbooker.feature.schedules.domain.useCase
import com.example.scrollbooker.feature.schedules.domain.model.Schedule
import com.example.scrollbooker.feature.schedules.domain.repository.ScheduleRepository
import com.example.scrollbooker.store.AuthDataStore
import com.example.scrollbooker.store.util.requireUserId
import javax.inject.Inject

class GetSchedulesUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val authDataStore: AuthDataStore
) {
    suspend operator fun invoke(): Result<List<Schedule>> = runCatching {
        val userId = authDataStore.requireUserId()
        scheduleRepository.getSchedules(userId)
    }
}