package com.example.scrollbooker.screens.auth.data.remote.userInfo
import retrofit2.http.GET

interface UserInfoApiService {
    @GET("auth/user-info")
    suspend fun getUserInfo(): UserInfoDto
}