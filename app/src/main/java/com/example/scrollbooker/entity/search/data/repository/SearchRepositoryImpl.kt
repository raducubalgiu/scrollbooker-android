package com.example.scrollbooker.entity.search.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.scrollbooker.entity.search.data.mappers.toDomain
import com.example.scrollbooker.entity.search.data.remote.SearchApiService
import com.example.scrollbooker.entity.search.data.remote.SearchUsersPagingSource
import com.example.scrollbooker.entity.search.data.remote.UserSearchCreateRequest
import com.example.scrollbooker.entity.search.domain.model.RecentlySearch
import com.example.scrollbooker.entity.search.domain.model.Search
import com.example.scrollbooker.entity.search.domain.model.UserSearch
import com.example.scrollbooker.entity.search.domain.repository.SearchRepository
import com.example.scrollbooker.entity.user.userSocial.data.mappers.toDomain
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: SearchApiService
): SearchRepository {
    override suspend fun search(query: String, lat: Float?, lng: Float?): List<Search> {
        return apiService.search(query, lng, lat).map { it.toDomain() }
    }

    override suspend fun searchUsers(
        query: String,
        roleClient: Boolean
    ): List<UserSocial> {
        return apiService.searchUsers(query, roleClient).map { it.toDomain() }
    }

    override suspend fun getUserSearch(
        lng: Float?,
        lat: Float?,
        timezone: String
    ): UserSearch {
        return apiService.getUserSearch(lng, lat, timezone).toDomain()
    }

    override suspend fun createUserSearch(keyword: String): RecentlySearch {
        val request = UserSearchCreateRequest(keyword)
        return apiService.createUserSearch(request).toDomain()
    }

    override suspend fun deleteUserSearch(searchId: Int) {
        return apiService.deleteUserSearch(searchId)
    }

    override fun searchPaginatedUsers(query: String): Flow<PagingData<UserSocial>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { SearchUsersPagingSource(apiService, query) }
        ).flow
    }
}