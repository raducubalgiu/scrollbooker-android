package com.example.scrollbooker.shared.currencies.data.remote

import retrofit2.http.GET

interface CurrenciesApiService {
    @GET("currencies")
    suspend fun getAllCurrencies(): List<CurrencyDto>

    @GET("users/{userId}/currencies")
    suspend fun getUserCurrencies(userId: Int): List<CurrencyDto>
}