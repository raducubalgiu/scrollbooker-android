package com.example.scrollbooker.shared.currency.domain.useCase
import com.example.scrollbooker.shared.currency.data.remote.CurrencyDto
import com.example.scrollbooker.shared.currency.domain.repository.CurrencyRepository
import javax.inject.Inject

class GetAllCurrenciesUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(): Result<List<CurrencyDto>> {
        return repository.getAllCurrencies()
    }
}