package com.example.scrollbooker.entity.currency.domain.useCase
import com.example.scrollbooker.entity.currency.data.remote.CurrencyDto
import com.example.scrollbooker.entity.currency.domain.repository.CurrencyRepository
import javax.inject.Inject

class GetAllCurrenciesUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(): Result<List<CurrencyDto>> {
        return repository.getAllCurrencies()
    }
}