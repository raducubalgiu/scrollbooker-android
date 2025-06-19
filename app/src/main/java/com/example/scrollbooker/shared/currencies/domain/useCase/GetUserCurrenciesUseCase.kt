package com.example.scrollbooker.shared.currencies.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.shared.currencies.domain.model.Currency
import com.example.scrollbooker.shared.currencies.domain.repository.CurrencyRepository
import timber.log.Timber
import javax.inject.Inject

class GetUserCurrenciesUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(userId: Int): FeatureState<List<Currency>> {
        return try {
            val response = repository.getUserCurrencies(userId)
            FeatureState.Success(response)
        } catch (e: Exception) {
            Timber.tag("Currencies").e("ERROR: on Fetching All Currencies: $e")
            FeatureState.Error(e)
        }
    }
}