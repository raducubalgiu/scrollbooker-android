package com.example.scrollbooker.entity.auth.data.repository

import com.example.scrollbooker.core.network.tokenProvider.TokenProvider
import com.example.scrollbooker.entity.auth.domain.model.LoginResponse
import com.example.scrollbooker.entity.auth.domain.repository.AuthRepository
import com.example.scrollbooker.entity.auth.data.mappers.toDoman
import com.example.scrollbooker.entity.auth.data.remote.AuthApiService
import com.example.scrollbooker.entity.auth.data.remote.AuthDto
import com.example.scrollbooker.entity.auth.data.remote.RoleNameEnum
import com.example.scrollbooker.store.AuthDataStore
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApiService,
    private val tokenProvider: TokenProvider,
    private val authDataStore: AuthDataStore
): AuthRepository {
    override suspend fun login(username: String, password: String): LoginResponse {
        val usernamePart = username.toRequestBody("text/plain".toMediaType())
        val passwordPart = password.toRequestBody("text/plain".toMediaType())

        return authApi.login(usernamePart, passwordPart).toDoman()
    }

    override suspend fun register(
        email: String,
        password: String,
        roleName: RoleNameEnum,
    ) {
        val request = AuthDto.RegisterDto(email, password, roleName)
        return authApi.register(request)
    }

    override suspend fun logout() {
        authDataStore.clearUserSession()
        tokenProvider.clearTokens()
    }
}