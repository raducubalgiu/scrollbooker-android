package com.example.scrollbooker.entity.user.userPermissions.data.remote

import retrofit2.http.GET

interface PermissionsApiService {
    @GET("auth/user-permissions/")
    suspend fun getUserPermissions(): List<PermissionDto>
}