package com.example.scrollbooker.entity.mapbbox.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.mapbbox.domain.model.Address
import com.example.scrollbooker.entity.mapbbox.domain.repository.MapboxRepository
import timber.log.Timber
import javax.inject.Inject

class SearchAddressUseCase @Inject constructor(
    private val repository: MapboxRepository
) {
    suspend operator fun invoke(query: String): FeatureState<List<Address>> {
        return try {
            val response = repository.searchAddress(query)
            FeatureState.Success(response)
        } catch (e: Exception) {
            Timber.tag("Search Address").e("ERROR: on Searching address $e")
            FeatureState.Error(e)
        }
    }
}