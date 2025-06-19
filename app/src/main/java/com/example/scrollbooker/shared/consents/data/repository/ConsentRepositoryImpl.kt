package com.example.scrollbooker.shared.consents.data.repository

import com.example.scrollbooker.shared.consents.data.mappers.toDomain
import com.example.scrollbooker.shared.consents.data.remote.ConsentsApiService
import com.example.scrollbooker.shared.consents.domain.model.Consent
import com.example.scrollbooker.shared.consents.domain.repository.ConsentRepository
import javax.inject.Inject

class ConsentRepositoryImpl @Inject constructor(
    private val apiService: ConsentsApiService
): ConsentRepository {
    override suspend fun getConsentByName(consentName: String): Consent {
        return apiService.getConsentsByName(consentName).toDomain()
    }
}