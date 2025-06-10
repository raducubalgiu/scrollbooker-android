package com.example.scrollbooker.feature.consents.data.repository

import com.example.scrollbooker.feature.consents.data.mappers.toDomain
import com.example.scrollbooker.feature.consents.data.remote.ConsentsApiService
import com.example.scrollbooker.feature.consents.domain.model.Consent
import com.example.scrollbooker.feature.consents.domain.repository.ConsentRepository
import javax.inject.Inject

class ConsentRepositoryImpl @Inject constructor(
    private val apiService: ConsentsApiService
): ConsentRepository {
    override suspend fun getConsentByName(consentName: String): Consent {
        return apiService.getConsentsByName(consentName).toDomain()
    }
}