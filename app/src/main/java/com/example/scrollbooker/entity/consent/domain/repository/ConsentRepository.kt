package com.example.scrollbooker.entity.consent.domain.repository

import com.example.scrollbooker.entity.consent.domain.model.Consent

interface ConsentRepository {
    suspend fun getConsentByName(consentName: String): Consent
}