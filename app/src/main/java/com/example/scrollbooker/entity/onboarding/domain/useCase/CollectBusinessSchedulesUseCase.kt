package com.example.scrollbooker.entity.onboarding.domain.useCase

import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.entity.onboarding.domain.repository.OnboardingRepository
import javax.inject.Inject

class CollectBusinessSchedulesUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository,
) {
    suspend operator fun invoke(schedules: List<Schedule>): Result<AuthState> = runCatching {
        onboardingRepository.collectBusinessSchedules(schedules)
    }
}