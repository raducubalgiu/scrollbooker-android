package com.example.scrollbooker.entity.auth.domain.useCase

import android.os.SystemClock
import com.example.scrollbooker.core.network.tokenProvider.TokenProvider
import com.example.scrollbooker.core.network.util.decodeJwtExpiry
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.user.userInfo.domain.useCase.GetUserInfoUseCase
import com.example.scrollbooker.store.AuthDataStore
import kotlinx.coroutines.flow.first
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
            val t0 = SystemClock.elapsedRealtime()
            val tokensPrefs = authDataStore.tokensPrefs.first()
            val accessToken = tokensPrefs.accessToken
            val refreshToken = tokensPrefs.refreshToken

            val t1 = SystemClock.elapsedRealtime()

            tokenProvider.updateTokens(
                accessToken = accessToken.orEmpty(),
                refreshToken = refreshToken.orEmpty()
            )
            val t2 = SystemClock.elapsedRealtime()

            val isValidAccessToken = isTokenValid(accessToken)
            val t3 = SystemClock.elapsedRealtime()

            if(isValidAccessToken) {
                val userInfo = getUserInfoUseCase()
                val t4 = SystemClock.elapsedRealtime()

                Timber.d(
                    "startup isLoggedIn: access&refresh=${t1-t0}ms update=${t2-t1}ms checkToken=${t3-t2} userInfo=${t4-t3}ms total=${t4-t0}ms"
                )

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