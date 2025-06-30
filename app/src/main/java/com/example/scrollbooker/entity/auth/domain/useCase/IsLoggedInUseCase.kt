package com.example.scrollbooker.entity.auth.domain.useCase

import com.example.scrollbooker.core.network.tokenProvider.TokenProvider
import com.example.scrollbooker.core.network.util.decodeJwtExpiry
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.auth.data.remote.AuthApiService
import com.example.scrollbooker.entity.auth.data.remote.AuthDto
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.user.userInfo.domain.useCase.GetUserInfoUseCase
import com.example.scrollbooker.store.AuthDataStore
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject

class IsLoggedInUseCase @Inject constructor(
    private val apiService: AuthApiService,
    private val tokenProvider: TokenProvider,
    private val authDataStore: AuthDataStore,
    private val getUserInfoUseCase: GetUserInfoUseCase
) {
    suspend operator fun invoke(): FeatureState<AuthState> {
        return try {
            val accessToken = authDataStore.getAccessToken().firstOrNull()
            val refreshToken = authDataStore.getRefreshToken().firstOrNull()

            Timber.tag("LOGGED IN ACCESS TOKEN").e("Access Token $accessToken")

            tokenProvider.updateTokens(accessToken.toString(), refreshToken)

            if(isTokenValid(accessToken)) {
                val userInfo = getUserInfoUseCase()

                Timber.tag("LOGGED IN USER INFO!!").e("User Info $userInfo")

                return FeatureState.Success(AuthState(
                    isValidated = userInfo.isValidated,
                    registrationStep = userInfo.registrationStep
                ))
            }

            if(isTokenValid(refreshToken) && !refreshToken.isNullOrBlank()) {
                return try {
                    val response = apiService.refresh(AuthDto.RefreshRequestDto(refreshToken))
                    authDataStore.refreshTokens(response.accessToken, response.refreshToken)
                    tokenProvider.updateTokens(response.accessToken, response.refreshToken)

                    val userInfo = getUserInfoUseCase()

                    return FeatureState.Success(AuthState(
                        isValidated = userInfo.isValidated,
                        registrationStep = userInfo.registrationStep
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