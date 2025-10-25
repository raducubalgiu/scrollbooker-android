package com.example.scrollbooker.entity.booking.business.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessLocation
import com.example.scrollbooker.entity.booking.business.domain.repository.BusinessRepository
import timber.log.Timber
import javax.inject.Inject

class GetBusinessLocationUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(
        businessId: Int,
        userLat: Float?,
        userLng: Float?
    ): FeatureState<BusinessLocation> {
        try {
            val response = repository.getBusinessLocation(businessId, userLat, userLng)
            return FeatureState.Success(response)

        } catch (e: Exception) {
            Timber.tag("Business").e("ERROR: on Fetching Business Location $e")
            return FeatureState.Error(e)
        }
    }
}