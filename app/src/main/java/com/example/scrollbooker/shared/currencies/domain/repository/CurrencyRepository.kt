package com.example.scrollbooker.shared.currencies.domain.repository

import com.example.scrollbooker.shared.currencies.data.remote.CurrencyDto

interface CurrencyRepository {
    suspend fun getAllCurrencies(): Result<List<CurrencyDto>>
    suspend fun getUserCurrencies(userId: Int): Result<List<CurrencyDto>>
}