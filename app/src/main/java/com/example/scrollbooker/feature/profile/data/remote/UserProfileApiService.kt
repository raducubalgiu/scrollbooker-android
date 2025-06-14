package com.example.scrollbooker.feature.profile.data.remote

import com.example.scrollbooker.feature.profile.domain.model.UpdateFullNameRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface UserProfileApiService {
    @GET("users/{userId}/user-profile")
    suspend fun getUserProfile(
        @Path("userId") userId: Int
    ): UserProfileDto

    @PATCH("users/user-info/fullname")
    suspend fun updateFullName(
        @Body request: UpdateFullNameRequest
    )
}