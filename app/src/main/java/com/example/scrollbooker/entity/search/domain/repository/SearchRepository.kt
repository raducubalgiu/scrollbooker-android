package com.example.scrollbooker.entity.search.domain.repository
import androidx.paging.PagingData
import com.example.scrollbooker.entity.search.domain.model.RecentlySearch
import com.example.scrollbooker.entity.search.domain.model.Search
import com.example.scrollbooker.entity.search.domain.model.UserSearch
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun search(query: String, lng: Float?, lat: Float?): List<Search>
    suspend fun searchUsers(query: String, roleClient: Boolean): List<UserSocial>
    suspend fun getUserSearch(lng: Float?, lat: Float?, timezone: String): UserSearch
    suspend fun createUserSearch(keyword: String): RecentlySearch
    suspend fun deleteUserSearch(searchId: Int)
    fun searchPaginatedUsers(query: String):  Flow<PagingData<UserSocial>>
}