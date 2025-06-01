package com.example.scrollbooker.feature.auth.data.remote

import retrofit2.http.GET

interface UserApiService {
    @GET("auth/user-info")
    suspend fun getUserInfo(): UserDto

    @GET("auth/user-permissions")
    suspend fun getUserPermissions(): List<PermissionDto>
}