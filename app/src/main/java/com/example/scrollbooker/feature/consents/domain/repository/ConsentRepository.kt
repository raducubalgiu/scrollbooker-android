package com.example.scrollbooker.feature.consents.domain.repository

import com.example.scrollbooker.feature.consents.domain.model.Consent

interface ConsentRepository {
    suspend fun getConsentByName(consentName: String): Consent
}