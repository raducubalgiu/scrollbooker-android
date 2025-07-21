package com.example.scrollbooker.entity.nomenclature.currency.domain.useCase
import com.example.scrollbooker.entity.nomenclature.currency.domain.model.Currency
import com.example.scrollbooker.entity.nomenclature.currency.domain.repository.CurrencyRepository
import javax.inject.Inject

class GetAllCurrenciesUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(): Result<List<Currency>> {
        return repository.getAllCurrencies()
    }
}