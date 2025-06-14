package com.example.scrollbooker.feature.auth.data.remote.userInfo
import retrofit2.http.GET

interface UserInfoApiService {
    @GET("auth/user-info")
    suspend fun getUserInfo(): UserInfoDto
}