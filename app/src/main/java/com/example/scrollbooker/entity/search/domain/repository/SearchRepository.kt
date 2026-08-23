package com.example.scrollbooker.entity.search.domain.repository
import com.example.scrollbooker.entity.search.domain.model.RecentSearch
import com.example.scrollbooker.entity.search.domain.model.SearchUser
interface SearchRepository {
    suspend fun searchUsers(query: String, roleClient: Boolean?): List<SearchUser>
    suspend fun getRecentSearch(limit: Int = 10): List<RecentSearch>
}