package com.example.scrollbooker.feature.auth.domain.repository

import com.example.scrollbooker.feature.auth.domain.model.LoginRequest
import com.example.scrollbooker.feature.auth.domain.model.LoginResponse

interface AuthRepository {
    suspend fun login(request: LoginRequest): Result<LoginResponse>
}