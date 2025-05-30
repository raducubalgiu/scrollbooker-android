package com.example.scrollbooker.feature.profile.data.remote

import retrofit2.http.GET

interface UserApiService {
    @GET("user-info")
    suspend fun getUserInfo(): UserDto
}