package com.example.scrollbooker.feature.consents.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface ConsentsApiService {
    @GET("consents/{consentName}")
    suspend fun getConsentsByName(
        @Path("consentName") consentName: String
    ): ConsentDto
}