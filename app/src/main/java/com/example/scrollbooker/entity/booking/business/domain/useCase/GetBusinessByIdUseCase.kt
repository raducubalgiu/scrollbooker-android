package com.example.scrollbooker.entity.booking.business.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.business.domain.model.Business
import com.example.scrollbooker.entity.booking.business.domain.repository.BusinessRepository
import timber.log.Timber
import javax.inject.Inject

class GetBusinessByIdUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(businessId: Int): FeatureState<Business> {
        try {
            val response = repository.getBusinessById(businessId)
            return FeatureState.Success(response)

        } catch (e: Exception) {
            Timber.tag("Business").e("ERROR: on Fetching Business $e")
            return FeatureState.Error(e)
        }
    }
}