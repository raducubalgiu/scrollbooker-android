package com.example.scrollbooker.shared.currencies.data.repository

import com.example.scrollbooker.shared.currencies.data.mapper.toDomain
import com.example.scrollbooker.shared.currencies.data.remote.CurrenciesApiService
import com.example.scrollbooker.shared.currencies.domain.model.Currency
import com.example.scrollbooker.shared.currencies.domain.repository.CurrencyRepository
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val apiService: CurrenciesApiService
): CurrencyRepository {
    override suspend fun getAllCurrencies(): List<Currency> {
        return apiService.getAllCurrencies().map { it.toDomain() }
    }

    override suspend fun getUserCurrencies(userId: Int): List<Currency> {
        return apiService.getUserCurrencies(userId).map { it.toDomain() }
    }

}