package com.example.scrollbooker.shared.currency.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface CurrenciesApiService {
    @GET("currencies")
    suspend fun getAllCurrencies(): List<CurrencyDto>

    @GET("users/{userId}/currencies")
    suspend fun getUserCurrencies(
        @Path("userId") userId: Int
    ): List<CurrencyDto>
}