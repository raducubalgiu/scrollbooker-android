package com.example.scrollbooker.entity.onboarding.domain.useCase

import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.onboarding.domain.repository.OnboardingRepository
import javax.inject.Inject

class CollectBusinessCurrenciesUseCase @Inject constructor(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke(currencyIds: List<Int>): Result<AuthState> = runCatching {
        repository.collectBusinessCurrencies(currencyIds)
    }
}