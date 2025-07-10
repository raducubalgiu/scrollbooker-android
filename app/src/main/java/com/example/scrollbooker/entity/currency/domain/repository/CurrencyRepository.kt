package com.example.scrollbooker.entity.currency.domain.repository

import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.currency.data.remote.CurrencyDto
import com.example.scrollbooker.entity.currency.domain.model.Currency

interface CurrencyRepository {
    suspend fun getAllCurrencies(): Result<List<Currency>>
    suspend fun getUserCurrencies(userId: Int): Result<List<Currency>>
    suspend fun updateUserCurrencies(currencyIds: List<Int>): AuthState
}