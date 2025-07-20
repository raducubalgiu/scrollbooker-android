package com.example.scrollbooker.entity.search.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.search.domain.model.Search
import com.example.scrollbooker.entity.search.domain.repository.SearchRepository
import timber.log.Timber
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(query: String): FeatureState<List<Search>> {
        return try {
            val response = repository.search(query)
            FeatureState.Success(response)
        } catch (e: Exception) {
            Timber.tag("Search").e("ERROR: on Searching keywords, users, services or business types $e")
            FeatureState.Error(e)
        }
    }
}