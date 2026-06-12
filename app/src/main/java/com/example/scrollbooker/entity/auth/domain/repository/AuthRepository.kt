package com.example.scrollbooker.entity.auth.domain.repository

import com.example.scrollbooker.entity.auth.data.remote.RoleNameEnum
import com.example.scrollbooker.entity.auth.domain.model.AuthResponse

interface AuthRepository {
    suspend fun login(username: String, password: String): AuthResponse
    suspend fun register(email: String, password: String, roleName: RoleNameEnum): AuthResponse
    suspend fun logout()
}