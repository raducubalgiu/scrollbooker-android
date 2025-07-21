package com.example.scrollbooker.entity.onboarding.domain.useCase

import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.onboarding.domain.repository.OnboardingRepository
import javax.inject.Inject

class CollectUserUsernameUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) {
    suspend operator fun invoke(username: String): Result<AuthState> = runCatching {
        onboardingRepository.collectUserUsername(username)
    }
}