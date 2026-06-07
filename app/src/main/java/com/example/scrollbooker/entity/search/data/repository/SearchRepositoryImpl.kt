package com.example.scrollbooker.entity.search.data.repository
import com.example.scrollbooker.entity.search.data.remote.SearchApiService
import com.example.scrollbooker.entity.search.domain.repository.SearchRepository
import com.example.scrollbooker.entity.user.userSocial.data.mappers.toDomain
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: SearchApiService
): SearchRepository {
    override suspend fun searchUsers(
        query: String,
        roleClient: Boolean
    ): List<UserSocial> {
        return apiService.searchUsers(query, roleClient).map { it.toDomain() }
    }
}