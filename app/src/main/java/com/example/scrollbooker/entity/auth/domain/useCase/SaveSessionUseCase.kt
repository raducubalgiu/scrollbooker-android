package com.example.scrollbooker.entity.auth.domain.useCase

import com.example.scrollbooker.core.network.tokenProvider.TokenProvider
import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.auth.domain.model.AuthResponse
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.store.AuthDataStore
import javax.inject.Inject

class SaveSessionUseCase @Inject constructor(
    private val tokenProvider: TokenProvider,
    private val authDataStore: AuthDataStore,
    private val saveUserSessionUseCase: SaveUserSessionUseCase
) {
    suspend operator fun invoke(authResponse: AuthResponse): Result<AuthState> =
        runSuspendCatching {
            authDataStore.refreshTokens(
                authResponse.accessToken,
                authResponse.refreshToken
            )
            tokenProvider.updateTokens(
                accessToken = authResponse.accessToken,
                refreshToken = authResponse.refreshToken
            )

            saveUserSessionUseCase().getOrThrow()
        }
}