package com.example.scrollbooker.entity.currency.domain.useCase

import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.currency.domain.repository.CurrencyRepository
import javax.inject.Inject

class UpdateUserCurrenciesUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(currencyIds: List<Int>): Result<AuthState> = runCatching {
        repository.updateUserCurrencies(currencyIds)
    }
}