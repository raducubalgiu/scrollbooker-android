package com.example.scrollbooker.entity.user.userInfo.data.remote
import retrofit2.http.GET

interface UserInfoApiService {
    @GET("auth/user-info")
    suspend fun getUserInfo(): UserInfoDto
}