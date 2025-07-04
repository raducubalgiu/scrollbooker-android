package com.example.scrollbooker.entity.currency.domain.repository

import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.currency.data.remote.CurrencyDto

interface CurrencyRepository {
    suspend fun getAllCurrencies(): Result<List<CurrencyDto>>
    suspend fun getUserCurrencies(userId: Int): Result<List<CurrencyDto>>
    suspend fun updateUserCurrencies(currencyIds: List<Int>): AuthState
}