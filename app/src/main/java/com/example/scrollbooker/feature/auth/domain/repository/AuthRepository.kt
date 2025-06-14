package com.example.scrollbooker.feature.auth.domain.repository

import com.example.scrollbooker.feature.auth.domain.model.LoginRequest
import com.example.scrollbooker.feature.auth.domain.model.LoginResponse
import com.example.scrollbooker.feature.auth.domain.model.UserInfo

interface AuthRepository {
    suspend fun loginAndSaveUserSession(request: LoginRequest): Result<LoginResponse>
    suspend fun isLoggedIn(): Boolean
    suspend fun logout(): Unit
}