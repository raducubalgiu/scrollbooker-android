package com.example.scrollbooker.screens.auth.domain.repository

import com.example.scrollbooker.screens.auth.domain.model.Permission

interface PermissionRepository {
    suspend fun getUserPermissions(): List<Permission>
}