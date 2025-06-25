package com.example.scrollbooker.shared.schedule.domain.useCase
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.shared.schedule.domain.model.Schedule
import com.example.scrollbooker.shared.schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.delay
import timber.log.Timber
import javax.inject.Inject

class GetSchedulesByUserIdUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {
    suspend operator fun invoke(userId: Int): FeatureState<List<Schedule>> {
        return try {
            delay(200)
            val schedules = scheduleRepository.getSchedules(userId)
            FeatureState.Success(schedules)
        } catch (e: Exception) {
            Timber.Forest.tag("Schedules").e(e, "ERROR: on Fetching Schedules By User Id")
            return FeatureState.Error()
        }
    }
}