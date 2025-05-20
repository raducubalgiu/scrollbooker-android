package com.example.scrollbooker.feature.auth.data.repository

import com.example.scrollbooker.feature.auth.data.remote.AuthApiService
import com.example.scrollbooker.feature.auth.data.remote.AuthDto
import com.example.scrollbooker.feature.auth.domain.model.LoginRequest
import com.example.scrollbooker.feature.auth.domain.model.LoginResponse
import com.example.scrollbooker.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService
): AuthRepository {
    override suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return try {
            val response = api.login(
                AuthDto.LoginRequestDto(
                    username = request.username,
                    password = request.password
                )
            )
            Result.success(
                LoginResponse(
                    accessToken = response.accessToken,
                    refreshToken = response.refreshToken,
                    userId = response.userId,
                    businessId = response.businessId
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}