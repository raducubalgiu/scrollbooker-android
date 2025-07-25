package com.example.scrollbooker.entity.booking.business.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.business.domain.model.RecommendedBusiness
import com.example.scrollbooker.entity.booking.business.domain.repository.BusinessRepository
import timber.log.Timber
import javax.inject.Inject

class GetRecommendedBusinessesUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(lng: Float?, lat: Float?, timezone: String): FeatureState<List<RecommendedBusiness>> {
        try {
            val response = repository.getRecommendedBusinesses(lng, lat, timezone)
            return FeatureState.Success(response)

        } catch (e: Exception) {
            Timber.tag("Recommended Businesses").e("ERROR: on Fetching Recommended Businesses $e")
            return FeatureState.Error(e)
        }
    }
}