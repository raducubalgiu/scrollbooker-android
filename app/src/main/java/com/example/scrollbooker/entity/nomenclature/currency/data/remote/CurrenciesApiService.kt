package com.example.scrollbooker.entity.nomenclature.currency.data.remote

import com.example.scrollbooker.entity.auth.data.remote.AuthStateDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface CurrenciesApiService {
    @GET("currencies/")
    suspend fun getAllCurrencies(): List<CurrencyDto>

    @GET("users/{userId}/currencies/")
    suspend fun getUserCurrencies(
        @Path("userId") userId: Int
    ): List<CurrencyDto>

    @PUT("users/update-currencies")
    suspend fun updateUserCurrencies(
        @Body request: UserCurrencyUpdateRequest
    ): AuthStateDto
}