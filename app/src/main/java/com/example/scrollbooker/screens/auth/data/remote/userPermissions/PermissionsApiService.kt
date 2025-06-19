package com.example.scrollbooker.screens.auth.data.remote.userPermissions

import retrofit2.http.GET

interface PermissionsApiService {
    @GET("auth/user-permissions/")
    suspend fun getUserPermissions(): List<PermissionDto>
}