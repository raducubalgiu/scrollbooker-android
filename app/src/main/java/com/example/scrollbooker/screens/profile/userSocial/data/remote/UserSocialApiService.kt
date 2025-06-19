package com.example.scrollbooker.screens.profile.userSocial.data.remote

import com.example.scrollbooker.core.util.PaginatedResponseDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
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

    @POST("follows/{followee_id}")
    suspend fun followUser(@Path("followee_id") followeeId: Int)

    @DELETE("follows/{followee_id}")
    suspend fun unfollowUser(@Path("followee_id") followeeId: Int)
}