package com.example.scrollbooker.shared.currencies.domain.useCase
import com.example.scrollbooker.shared.currencies.data.remote.CurrencyDto
import com.example.scrollbooker.shared.currencies.domain.repository.CurrencyRepository
import javax.inject.Inject

class GetAllCurrenciesUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(): Result<List<CurrencyDto>> {
        return repository.getAllCurrencies()
    }
}