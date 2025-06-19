package com.example.scrollbooker.screens.auth.data.repository
import com.example.scrollbooker.screens.auth.data.mappers.toDomain
import com.example.scrollbooker.screens.auth.data.remote.userPermissions.PermissionsApiService
import com.example.scrollbooker.screens.auth.domain.model.Permission
import com.example.scrollbooker.screens.auth.domain.repository.PermissionRepository
import javax.inject.Inject

class PermissionRepositoryImpl @Inject constructor(
    private val apiService: PermissionsApiService
): PermissionRepository {
    override suspend fun getUserPermissions(): List<Permission> {
        return apiService.getUserPermissions().toDomain()
    }
}