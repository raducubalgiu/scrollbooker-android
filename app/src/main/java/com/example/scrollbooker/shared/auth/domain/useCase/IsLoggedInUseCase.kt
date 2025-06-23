package com.example.scrollbooker.shared.auth.domain.useCase

import com.example.scrollbooker.core.network.tokenProvider.TokenProvider
import com.example.scrollbooker.core.network.util.decodeJwtExpiry
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.shared.auth.data.remote.AuthApiService
import com.example.scrollbooker.shared.auth.data.remote.AuthDto
import com.example.scrollbooker.shared.auth.domain.model.AuthState
import com.example.scrollbooker.store.AuthDataStore
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject

class IsLoggedInUseCase @Inject constructor(
    private val apiService: AuthApiService,
    private val tokenProvider: TokenProvider,
    private val authDataStore: AuthDataStore,
) {
    suspend operator fun invoke(): FeatureState<AuthState> {
        return try {
            val accessToken = authDataStore.getAccessToken().firstOrNull()
            val refreshToken = authDataStore.getRefreshToken().firstOrNull()
            val isValidated = authDataStore.getIsValidated().firstOrNull()

            if(isValidated == null) {
                throw IllegalStateException("Is Validated Not found in DataStore")
            }

            val registrationStep = if(isValidated) null
                else authDataStore.getRegistrationStep().firstOrNull()

            tokenProvider.updateTokens(accessToken.toString(), refreshToken)

            if(isTokenValid(accessToken)) {
                FeatureState.Success(AuthState(
                    isValidated,
                    registrationStep = registrationStep
                ))
            } else {
                if(isTokenValid(refreshToken) && !refreshToken.isNullOrBlank()) {
                    return try {
                        val response = apiService.refresh(AuthDto.RefreshRequestDto(refreshToken))
                        authDataStore.refreshTokens(response.accessToken, response.refreshToken)
                        tokenProvider.updateTokens(response.accessToken, response.refreshToken)

                        FeatureState.Success(AuthState(
                            isValidated,
                            registrationStep = registrationStep
                        ))
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