package com.example.scrollbooker.shared.consent.data.repository

import com.example.scrollbooker.shared.consent.data.mappers.toDomain
import com.example.scrollbooker.shared.consent.data.remote.ConsentsApiService
import com.example.scrollbooker.shared.consent.domain.model.Consent
import com.example.scrollbooker.shared.consent.domain.repository.ConsentRepository
import javax.inject.Inject

class ConsentRepositoryImpl @Inject constructor(
    private val apiService: ConsentsApiService
): ConsentRepository {
    override suspend fun getConsentByName(consentName: String): Consent {
        return apiService.getConsentsByName(consentName).toDomain()
    }
}