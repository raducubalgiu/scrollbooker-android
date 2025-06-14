package com.example.scrollbooker.feature.auth.domain.usecase

import com.example.scrollbooker.feature.auth.data.remote.auth.AuthApiService
import com.example.scrollbooker.feature.auth.data.remote.auth.AuthDto
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class GetLoginUseCase @Inject constructor(
    private val authApi: AuthApiService
) {
    suspend operator fun invoke(username: String, password: String): AuthDto.LoginResponseDto {
        val usernamePart = username.toRequestBody("text/plain".toMediaType())
        val passwordPart = password.toRequestBody("text/plain".toMediaType())

        return authApi.getLoginInfo(usernamePart, passwordPart)
    }
}