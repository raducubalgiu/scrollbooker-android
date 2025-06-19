package com.example.scrollbooker.shared.currency.domain.repository

import com.example.scrollbooker.shared.currency.data.remote.CurrencyDto

interface CurrencyRepository {
    suspend fun getAllCurrencies(): Result<List<CurrencyDto>>
    suspend fun getUserCurrencies(userId: Int): Result<List<CurrencyDto>>
}