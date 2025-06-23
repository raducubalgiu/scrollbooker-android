package com.example.scrollbooker.shared.auth.domain.repository

import com.example.scrollbooker.shared.auth.domain.model.LoginResponse

interface AuthRepository {
    suspend fun login(username: String, password: String): LoginResponse
    suspend fun register(email: String, username: String, password: String, roleName: String, isValidated: Boolean)
    suspend fun logout()
}