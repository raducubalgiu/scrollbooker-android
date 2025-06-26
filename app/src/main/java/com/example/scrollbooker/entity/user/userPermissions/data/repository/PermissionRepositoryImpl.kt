package com.example.scrollbooker.entity.user.userPermissions.data.repository

import com.example.scrollbooker.entity.user.userPermissions.domain.model.Permission
import com.example.scrollbooker.entity.user.userPermissions.domain.repository.PermissionRepository
import com.example.scrollbooker.entity.user.userPermissions.data.mappers.toDomain
import com.example.scrollbooker.entity.user.userPermissions.data.remote.PermissionsApiService
import javax.inject.Inject

class PermissionRepositoryImpl @Inject constructor(
    private val apiService: PermissionsApiService
): PermissionRepository {
    override suspend fun getUserPermissions(): List<Permission> {
        return apiService.getUserPermissions().toDomain()
    }
}