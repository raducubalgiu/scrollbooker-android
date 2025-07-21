package com.example.scrollbooker.entity.nomenclature.consent.domain.repository

import com.example.scrollbooker.entity.nomenclature.consent.domain.model.Consent

interface ConsentRepository {
    suspend fun getConsentByName(consentName: String): Consent
}