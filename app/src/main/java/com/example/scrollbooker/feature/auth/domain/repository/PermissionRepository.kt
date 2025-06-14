package com.example.scrollbooker.feature.auth.domain.repository

import com.example.scrollbooker.feature.auth.domain.model.Permission

interface PermissionRepository {
    suspend fun getUserPermissions(): List<Permission>
}