package com.example.scrollbooker.feature.profile.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface UserProfileApiService {
    @GET("users/{userId}/user-profile")
    suspend fun getUserProfile(
        @Path("userId") userId: Int
    ): UserProfileDto
}