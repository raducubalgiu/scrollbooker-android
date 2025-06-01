package com.example.scrollbooker.feature.myBusiness.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.feature.myBusiness.domain.model.Schedule
import com.example.scrollbooker.feature.myBusiness.domain.repository.ScheduleRepository
import com.example.scrollbooker.store.AuthDataStore
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject

class GetSchedulesUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val authDataStore: AuthDataStore
) {
    suspend operator fun invoke(): FeatureState<List<Schedule>> {
        return try {
            val userId = authDataStore.getUserId().firstOrNull()

            if(userId == null) {
                Timber.tag("Schedules").e("ERROR: User Id not found in DataStore")
                return FeatureState.Error()
            }

            val schedules = scheduleRepository.getSchedules(userId)
            FeatureState.Success(schedules)
        } catch (e: Exception) {
            Timber.tag("Schedules").e(e, "ERROR: on Fetching Schedules")
            return FeatureState.Error()
        }
    }
}