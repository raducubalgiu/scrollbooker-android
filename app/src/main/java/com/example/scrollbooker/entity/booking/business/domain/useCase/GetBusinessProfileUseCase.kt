package com.example.scrollbooker.entity.booking.business.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfile
import com.example.scrollbooker.entity.booking.business.domain.repository.BusinessRepository
import timber.log.Timber
import javax.inject.Inject

class GetBusinessProfileUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(businessId: Int): FeatureState<BusinessProfile> {
        try {
            val response = repository.getBusinessProfileById(businessId)
            return FeatureState.Success(response)

        } catch (e: Exception) {
            Timber.tag("Business Profile").e(e, "ERROR: on Fetching Business Profile")
            return FeatureState.Error(e)
        }
    }
}