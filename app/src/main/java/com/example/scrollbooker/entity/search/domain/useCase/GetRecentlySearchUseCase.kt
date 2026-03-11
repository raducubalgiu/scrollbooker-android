package com.example.scrollbooker.entity.search.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.search.domain.model.RecentlySearch
import com.example.scrollbooker.entity.search.domain.repository.SearchRepository
import timber.log.Timber
import javax.inject.Inject

class GetRecentlySearchUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(): FeatureState<List<RecentlySearch>> {
        return try {
            val response = repository.getRecentlySearch()
            FeatureState.Success(response)
        } catch (e: Exception) {
            Timber.tag("User Search").e(e, "ERROR: on Fetching User Recently Search")
            FeatureState.Error(e)
        }
    }
}