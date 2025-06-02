package com.example.scrollbooker.feature.myBusiness.domain.useCase.schedules
import com.example.scrollbooker.feature.myBusiness.domain.model.Schedule
import com.example.scrollbooker.feature.myBusiness.domain.repository.ScheduleRepository
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