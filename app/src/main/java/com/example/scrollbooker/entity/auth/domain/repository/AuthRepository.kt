package com.example.scrollbooker.entity.auth.domain.repository

import com.example.scrollbooker.entity.auth.data.remote.RoleNameEnum
import com.example.scrollbooker.entity.auth.domain.model.LoginResponse

interface AuthRepository {
    suspend fun login(username: String, password: String): LoginResponse
    suspend fun register(email: String, password: String, roleName: RoleNameEnum): LoginResponse
    suspend fun logout()
}