package com.example.scrollbooker.entity.onboarding.domain.useCase

import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.onboarding.domain.repository.OnboardingRepository
import javax.inject.Inject

class CollectClientBirthDateUseCase @Inject constructor(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke(birthdate: String?): Result<AuthState> = runCatching {
        repository.collectClientBirthDate(birthdate)
    }
}