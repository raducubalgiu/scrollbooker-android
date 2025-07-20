package com.example.scrollbooker.entity.search.data.remote
import com.example.scrollbooker.core.util.PaginatedResponseDto
import com.example.scrollbooker.entity.user.userSocial.data.remote.UserSocialDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {
    @GET("search")
    suspend fun search(
        @Query("query") query: String,
        @Query("lng") lng: Float?,
        @Query("lat") lat: Float?
    ): List<SearchDto>

    @GET("search/users")
    suspend fun searchUsers(
        @Query("query") query: String,
        @Query("role_client") roleClient: Boolean
    ): List<UserSocialDto>

    @GET("search/users")
    suspend fun searchPaginatedUsers(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponseDto<UserSocialDto>
}