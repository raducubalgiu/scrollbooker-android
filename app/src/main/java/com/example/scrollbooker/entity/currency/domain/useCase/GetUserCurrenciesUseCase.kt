package com.example.scrollbooker.entity.currency.domain.useCase
import com.example.scrollbooker.entity.currency.domain.model.Currency
import com.example.scrollbooker.entity.currency.domain.repository.CurrencyRepository
import javax.inject.Inject

class GetUserCurrenciesUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(userId: Int): Result<List<Currency>> {
        return repository.getUserCurrencies(userId)
    }
}