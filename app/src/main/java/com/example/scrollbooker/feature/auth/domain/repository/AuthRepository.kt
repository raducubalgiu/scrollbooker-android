package com.example.scrollbooker.feature.auth.domain.repository
import com.example.scrollbooker.feature.auth.domain.model.LoginResponse

interface AuthRepository {
    suspend fun login(username: String, password: String): LoginResponse
    suspend fun isLoggedIn(): Boolean
    suspend fun logout()
}