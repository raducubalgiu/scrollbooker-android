package com.example.scrollbooker.entity.onboarding.domain.useCase

import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.onboarding.domain.repository.OnboardingRepository
import javax.inject.Inject

class CollectBusinessServicesUseCase @Inject constructor(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke(serviceIds: List<Int>): Result<AuthState> = runCatching {
        repository.collectBusinessServices(serviceIds)
    }
}