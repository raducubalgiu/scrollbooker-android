package com.example.scrollbooker.feature.userSocial.data.remote

import com.example.scrollbooker.core.util.PaginatedResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserSocialApiService {
    @GET("users/{userId}/followers")
    suspend fun getUserFollowers(
        @Path("userId") userId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponseDto<UserSocialDto>

    @GET("users/{userId}/followings")
    suspend fun getUserFollowings(
        @Path("userId") userId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponseDto<UserSocialDto>
}