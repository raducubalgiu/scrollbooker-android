package com.example.scrollbooker.feature.profile.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserApiService {
    @GET("user-info")
    suspend fun getUserInfo(): UserDto

    @PATCH("username")
    suspend fun updateUsername(@Body body: Map<String, String>): UserDto

    @PATCH("full-name")
    suspend fun updateFullName(@Body body: Map<String, String>): UserDto
}