package com.example.scrollbooker.entity.search.data.repository

import com.example.scrollbooker.entity.search.data.mappers.toDomain
import com.example.scrollbooker.entity.search.data.remote.SearchApiService
import com.example.scrollbooker.entity.search.domain.model.Search
import com.example.scrollbooker.entity.search.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: SearchApiService
): SearchRepository {
    override suspend fun search(query: String): List<Search> {
        return apiService.search(query).map { it.toDomain() }
    }
}