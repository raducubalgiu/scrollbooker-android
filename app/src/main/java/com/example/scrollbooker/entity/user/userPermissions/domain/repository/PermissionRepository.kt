package com.example.scrollbooker.entity.user.userPermissions.domain.repository

import com.example.scrollbooker.entity.user.userPermissions.domain.model.Permission

interface PermissionRepository {
    suspend fun getUserPermissions(): List<Permission>
}