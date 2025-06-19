package com.example.scrollbooker.shared.schedules.domain.useCase
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.shared.schedules.domain.model.Schedule
import com.example.scrollbooker.shared.schedules.domain.repository.ScheduleRepository
import timber.log.Timber
import javax.inject.Inject

class GetSchedulesByUserIdUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {
    suspend operator fun invoke(userId: Int): FeatureState<List<Schedule>> {
        return try {
            val schedules = scheduleRepository.getSchedules(userId)
            FeatureState.Success(schedules)
        } catch (e: Exception) {
            Timber.Forest.tag("Schedules").e(e, "ERROR: on Fetching Schedules By User Id")
            return FeatureState.Error()
        }
    }
}