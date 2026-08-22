package com.example.scrollbooker.entity.auth.domain.useCase

import com.example.scrollbooker.core.network.tokenProvider.TokenProvider
import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.auth.data.remote.AuthApiService
import com.example.scrollbooker.entity.auth.data.remote.AuthRequestDto
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.user.userInfo.domain.useCase.GetUserInfoUseCase
import com.example.scrollbooker.store.AuthDataStore
import timber.log.Timber
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val apiService: AuthApiService,
    private val authDataStore: AuthDataStore,
    private val tokenProvider: TokenProvider,
    private val getUserInfoUseCase: GetUserInfoUseCase
) {
    suspend operator fun invoke(refreshToken: String): Result<AuthState> =
        runSuspendCatching {
            val response = apiService.refresh(AuthRequestDto.RefreshRequestDto(refreshToken))

            authDataStore.refreshTokens(response.accessToken, response.refreshToken)
            tokenProvider.updateTokens(response.accessToken, response.refreshToken)

            val userInfo = getUserInfoUseCase()

            AuthState(
                isValidated = userInfo.isValidated,
                registrationStep = userInfo.registrationStep
            )
        }.onFailure { e ->
            Timber.tag("Refresh Token").e(e, "ERROR: on attempting to refresh token")
            authDataStore.clearUserSession()
            tokenProvider.clearTokens()
        }
}