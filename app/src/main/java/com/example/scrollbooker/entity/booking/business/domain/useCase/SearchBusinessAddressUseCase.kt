package com.example.scrollbooker.entity.booking.business.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessAddress
import com.example.scrollbooker.entity.booking.business.domain.repository.BusinessRepository
import timber.log.Timber
import javax.inject.Inject

class SearchBusinessAddressUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(query: String): FeatureState<List<BusinessAddress>> {
        return try {
            val response = repository.searchBusinessAddress(query)
            FeatureState.Success(response)
        } catch (e: Exception) {
            Timber.tag("Search Business Address").e("ERROR: on Searching business address $e")
            FeatureState.Error(e)
        }
    }
}