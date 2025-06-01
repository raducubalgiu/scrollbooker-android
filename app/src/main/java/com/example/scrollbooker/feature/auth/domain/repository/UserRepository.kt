package com.example.scrollbooker.feature.auth.domain.repository

import com.example.scrollbooker.feature.auth.domain.model.Permission
import com.example.scrollbooker.feature.auth.domain.model.User

interface UserRepository {
    suspend fun getUserInfo(): User
    suspend fun getUserPermissions(): List<Permission>
}