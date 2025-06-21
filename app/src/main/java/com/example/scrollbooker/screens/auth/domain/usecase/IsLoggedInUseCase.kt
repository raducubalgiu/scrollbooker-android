package com.example.scrollbooker.screens.auth.domain.usecase

import com.example.scrollbooker.core.network.tokenProvider.TokenProvider
import com.example.scrollbooker.core.network.util.decodeJwtExpiry
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.screens.auth.AuthStateDto
import com.example.scrollbooker.screens.auth.data.remote.auth.AuthApiService
import com.example.scrollbooker.screens.auth.data.remote.auth.AuthDto
import com.example.scrollbooker.store.AuthDataStore
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject

class IsLoggedInUseCase @Inject constructor(
    private val apiService: AuthApiService,
    private val tokenProvider: TokenProvider,
    private val authDataStore: AuthDataStore,
) {
    suspend operator fun invoke(): FeatureState<AuthStateDto> {
        return try {
            val accessToken = authDataStore.getAccessToken().firstOrNull()
            val refreshToken = authDataStore.getRefreshToken().firstOrNull()

            tokenProvider.updateTokens(accessToken.toString(), refreshToken)

            if(isTokenValid(accessToken)) {
                FeatureState.Success(AuthStateDto(isValidated = false))
            } else {
                if(isTokenValid(refreshToken) && !refreshToken.isNullOrBlank()) {
                    return try {
                        val response = apiService.refresh(AuthDto.RefreshRequestDto(refreshToken))
                        authDataStore.refreshTokens(response.accessToken, response.refreshToken)
                        tokenProvider.updateTokens(response.accessToken, response.refreshToken)

                        FeatureState.Success(AuthStateDto(isValidated = false))
                    } catch (e: Exception) {
                        Timber.tag("Refresh Token").e(e, "ERROR: on attempting to refresh token")
                        authDataStore.clearUserSession()
                        tokenProvider.clearTokens()

                        FeatureState.Error(e)
                    }
                } else {
                    authDataStore.clearUserSession()
                    tokenProvider.clearTokens()

                    FeatureState.Error()
                }
            }

        } catch (e: Exception) {
            Timber.tag("Is Logged In").e(e, "ERROR: on Trying to get loggedIn Status")
            FeatureState.Error()
        }
    }

    private fun isTokenValid(token: String?): Boolean {
        val expiry = token?.let { decodeJwtExpiry(it) }
        return expiry != null && System.currentTimeMillis() < expiry
    }
}