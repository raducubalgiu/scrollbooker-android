package com.example.scrollbooker.entity.consent.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.consent.domain.model.Consent
import com.example.scrollbooker.entity.consent.domain.repository.ConsentRepository
import timber.log.Timber

class GetConsentsByNameUseCase(
    private val repository: ConsentRepository,
) {
    suspend operator fun invoke(consentName: String): FeatureState<Consent> {
        return try {
            val response = repository.getConsentByName(consentName)
            FeatureState.Success(response)

        } catch (e: Exception) {
            Timber.tag("Consents").e("ERROR: on Fetching Consents: $e")
            FeatureState.Error(e)
        }
    }
}