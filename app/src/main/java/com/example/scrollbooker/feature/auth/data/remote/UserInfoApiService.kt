package com.example.scrollbooker.feature.auth.data.remote

import retrofit2.http.POST

interface UserInfoApiService {
    @POST("auth/user-info/")
    suspend fun getUserInfo(): UserInfoDto
}