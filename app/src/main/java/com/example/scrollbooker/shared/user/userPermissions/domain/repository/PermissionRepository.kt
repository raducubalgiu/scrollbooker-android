package com.example.scrollbooker.shared.user.userPermissions.domain.repository

import com.example.scrollbooker.shared.user.userPermissions.domain.model.Permission

interface PermissionRepository {
    suspend fun getUserPermissions(): List<Permission>
}