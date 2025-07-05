package com.example.scrollbooker.entity.auth.domain.useCase

import com.example.scrollbooker.core.network.tokenProvider.TokenProvider
import com.example.scrollbooker.core.network.util.decodeJwtExpiry
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.user.userInfo.domain.useCase.GetUserInfoUseCase
import com.example.scrollbooker.store.AuthDataStore
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject

class IsLoggedInUseCase @Inject constructor(
    private val tokenProvider: TokenProvider,
    private val authDataStore: AuthDataStore,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase
) {
    suspend operator fun invoke(): FeatureState<AuthState> {
        return try {
            val accessToken = authDataStore.getAccessToken().firstOrNull()
            val refreshToken = authDataStore.getRefreshToken().firstOrNull()

            tokenProvider.updateTokens(accessToken.toString(), refreshToken)

            if(isTokenValid(accessToken)) {
                val userInfo = getUserInfoUseCase()

                return FeatureState.Success(AuthState(
                    isValidated = userInfo.isValidated,
                    registrationStep = userInfo.registrationStep
                ))
            }

            if(isTokenValid(refreshToken) && !refreshToken.isNullOrBlank()) {
                refreshTokenUseCase(refreshToken)
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