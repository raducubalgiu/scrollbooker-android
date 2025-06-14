package com.example.scrollbooker.feature.auth.data.repository
import com.example.scrollbooker.feature.auth.data.mappers.toDomain
import com.example.scrollbooker.feature.auth.data.remote.userPermissions.PermissionsApiService
import com.example.scrollbooker.feature.auth.domain.model.Permission
import com.example.scrollbooker.feature.auth.domain.repository.PermissionRepository
import javax.inject.Inject

class PermissionRepositoryImpl @Inject constructor(
    private val apiService: PermissionsApiService
): PermissionRepository {
    override suspend fun getUserPermissions(): List<Permission> {
        return apiService.getUserPermissions().toDomain()
    }
}