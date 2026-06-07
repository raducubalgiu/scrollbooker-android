package com.example.scrollbooker.entity.search.data.remote
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {
    @GET("search/users")
    suspend fun searchUsers(
        @Query("query") query: String,
        @Query("role_client") roleClient: Boolean? = null
    ): List<SearchUserDto>
}