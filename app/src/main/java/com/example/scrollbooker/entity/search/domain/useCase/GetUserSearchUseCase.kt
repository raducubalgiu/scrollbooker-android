package com.example.scrollbooker.entity.search.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.search.domain.model.UserSearch
import com.example.scrollbooker.entity.search.domain.repository.SearchRepository
import timber.log.Timber
import javax.inject.Inject

class GetUserSearchUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(lat: Float?, lng: Float?, timezone: String): FeatureState<UserSearch> {
        return try {
            val response = repository.getUserSearch(lat, lng, timezone)
            FeatureState.Success(response)
        } catch (e: Exception) {
            Timber.tag("User Search").e("ERROR: on Fetching User Search $e")
            FeatureState.Error(e)
        }
    }
}