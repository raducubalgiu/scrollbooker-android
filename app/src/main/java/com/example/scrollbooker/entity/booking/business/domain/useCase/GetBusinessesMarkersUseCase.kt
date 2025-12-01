package com.example.scrollbooker.entity.booking.business.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.PaginatedResponseDto
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessMarkersRequest
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessMarker
import com.example.scrollbooker.entity.booking.business.domain.repository.BusinessRepository
import timber.log.Timber
import javax.inject.Inject

class GetBusinessesMarkersUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(request: BusinessMarkersRequest): FeatureState<PaginatedResponseDto<BusinessMarker>> {
        try {
            val response = repository.getBusinessesMarkers(request)
            return FeatureState.Success(response)

        } catch (e: Exception) {
            Timber.tag("Business").e("ERROR: on Fetching Businesses Markers $e")
            return FeatureState.Error(e)
        }
    }
}