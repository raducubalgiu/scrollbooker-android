package com.example.scrollbooker.entity.search.data.repository
import com.example.scrollbooker.entity.search.data.mappers.toDomain
import com.example.scrollbooker.entity.search.data.remote.SearchApiService
import com.example.scrollbooker.entity.search.domain.model.SearchUser
import com.example.scrollbooker.entity.search.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: SearchApiService
): SearchRepository {
    override suspend fun searchUsers(
        query: String,
        roleClient: Boolean?
    ): List<SearchUser> {
        return apiService.searchUsers(query, roleClient).map { it.toDomain() }
    }
}