package com.example.scrollbooker.shared.currencies.domain.repository

import com.example.scrollbooker.shared.currencies.domain.model.Currency

interface CurrencyRepository {
    suspend fun getAllCurrencies(): List<Currency>
    suspend fun getUserCurrencies(userId: Int): List<Currency>
}