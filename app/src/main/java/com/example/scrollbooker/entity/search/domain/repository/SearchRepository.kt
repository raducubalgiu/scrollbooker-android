package com.example.scrollbooker.entity.search.domain.repository
import androidx.paging.PagingData
import com.example.scrollbooker.entity.search.domain.model.RecentlySearch
import com.example.scrollbooker.entity.search.domain.model.Search
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun search(query: String): List<Search>
    suspend fun searchUsers(query: String, roleClient: Boolean): List<UserSocial>
    suspend fun getRecentlySearch(): List<RecentlySearch>
    suspend fun createUserSearch(keyword: String): RecentlySearch
    suspend fun deleteUserSearch(searchId: Int)
    fun searchPaginatedUsers(query: String):  Flow<PagingData<UserSocial>>
}