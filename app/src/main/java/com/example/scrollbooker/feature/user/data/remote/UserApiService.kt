package com.example.scrollbooker.feature.user.data.remote

import com.example.scrollbooker.feature.auth.data.remote.PermissionDto
import retrofit2.http.GET

interface UserApiService {
    @GET("auth/user-info")
    suspend fun getUserInfo(): UserDto

    @GET("auth/user-permissions")
    suspend fun getUserPermissions(): List<PermissionDto>
}