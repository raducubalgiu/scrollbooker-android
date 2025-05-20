package com.example.scrollbooker.feature.auth.domain.usecase

import com.example.scrollbooker.feature.auth.domain.model.LoginRequest
import com.example.scrollbooker.feature.auth.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(request: LoginRequest) = repository.login(request)
}