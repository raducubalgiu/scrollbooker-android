package com.example.scrollbooker.feature.auth.data.repository

import android.util.Log
import com.example.scrollbooker.core.nav.routes.AuthRoute
import com.example.scrollbooker.feature.auth.data.remote.AuthApiService
import com.example.scrollbooker.feature.auth.data.remote.AuthDto
import com.example.scrollbooker.feature.auth.domain.model.LoginRequest
import com.example.scrollbooker.feature.auth.domain.model.LoginResponse
import com.example.scrollbooker.feature.auth.domain.repository.AuthRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService
): AuthRepository {
    override suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return try {
            Log.d("Auth Repo", "Sending login request")
            val usernamePart = request.username.toRequestBody("text/plain".toMediaType())
            val passwordPart = request.password.toRequestBody("text/plain".toMediaType())

            val response = api.login(usernamePart, passwordPart)

            Log.d("AuthRepo", "Login succesfully")

            Result.success(
                LoginResponse(
                    accessToken = response.accessToken,
                    refreshToken = response.refreshToken,
                    userId = response.userId,
                    businessId = response.businessId
                )
            )
        } catch (e: Exception) {
            Log.d("AuthRepo", "Login failed", e)
            Result.failure(e)
        }
    }
}