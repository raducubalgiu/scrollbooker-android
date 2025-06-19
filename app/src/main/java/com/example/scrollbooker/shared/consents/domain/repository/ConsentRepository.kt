package com.example.scrollbooker.shared.consents.domain.repository

import com.example.scrollbooker.shared.consents.domain.model.Consent

interface ConsentRepository {
    suspend fun getConsentByName(consentName: String): Consent
}