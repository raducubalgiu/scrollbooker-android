package com.example.scrollbooker.entity.consent.data.repository

import com.example.scrollbooker.entity.consent.data.mappers.toDomain
import com.example.scrollbooker.entity.consent.data.remote.ConsentsApiService
import com.example.scrollbooker.entity.consent.domain.model.Consent
import com.example.scrollbooker.entity.consent.domain.repository.ConsentRepository
import javax.inject.Inject

class ConsentRepositoryImpl @Inject constructor(
    private val apiService: ConsentsApiService
): ConsentRepository {
    override suspend fun getConsentByName(consentName: String): Consent {
        return apiService.getConsentsByName(consentName).toDomain()
    }
}