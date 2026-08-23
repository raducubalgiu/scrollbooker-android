package com.example.scrollbooker.entity.auth.domain.useCase

import com.example.scrollbooker.core.network.tokenProvider.TokenProvider
import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.auth.domain.model.AuthResponse
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import javax.inject.Inject

class SaveSessionUseCase @Inject constructor(
    private val tokenProvider: TokenProvider,
    private val saveUserSessionUseCase: SaveUserSessionUseCase
) {
    suspend operator fun invoke(authResponse: AuthResponse): Result<AuthState> =
        runSuspendCatching {
            tokenProvider.updateTokens(
                accessToken = authResponse.accessToken,
                refreshToken = authResponse.refreshToken
            )

            saveUserSessionUseCase().getOrThrow()
        }
}