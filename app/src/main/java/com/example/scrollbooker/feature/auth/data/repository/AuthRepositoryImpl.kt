package com.example.scrollbooker.feature.auth.data.repository
import com.example.scrollbooker.core.network.tokenProvider.TokenProvider
import com.example.scrollbooker.core.network.util.decodeJwtExpiry
import com.example.scrollbooker.feature.auth.data.remote.auth.AuthApiService
import com.example.scrollbooker.feature.auth.data.remote.auth.AuthDto
import com.example.scrollbooker.feature.auth.domain.model.LoginResponse
import com.example.scrollbooker.feature.auth.domain.repository.AuthRepository
import com.example.scrollbooker.store.AuthDataStore
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApiService,
    private val tokenProvider: TokenProvider,
    private val authDataStore: AuthDataStore
): AuthRepository {
    override suspend fun login(username: String, password: String): LoginResponse {
        val usernamePart = username.toRequestBody("text/plain".toMediaType())
        val passwordPart = password.toRequestBody("text/plain".toMediaType())
        return authApi.login(usernamePart, passwordPart)
    }

    override suspend fun isLoggedIn(): Boolean {
        val accessToken = authDataStore.getAccessToken().firstOrNull()
        val refreshToken = authDataStore.getRefreshToken().firstOrNull()

        tokenProvider.updateTokens(accessToken.toString(), refreshToken)

        if(isTokenValid(accessToken)) return true

        if(isTokenValid(refreshToken) && !refreshToken.isNullOrBlank()) {
            return try {
                val response = authApi.refresh(AuthDto.RefreshRequestDto(refreshToken))
                authDataStore.refreshTokens(response.accessToken, response.refreshToken)
                tokenProvider.updateTokens(response.accessToken, response.refreshToken)

                true
            } catch (e: Exception) {
                Timber.tag("Refresh Token").e(e, "ERROR: on attempting to refresh token")
                authDataStore.clearUserSession()
                tokenProvider.clearTokens()

                false
            }
        }

        authDataStore.clearUserSession()
        tokenProvider.clearTokens()
        return false
    }

    override suspend fun logout() {
        authDataStore.clearUserSession()
        tokenProvider.clearTokens()
    }

    private fun isTokenValid(token: String?): Boolean {
        val expiry = token?.let { decodeJwtExpiry(it) }
        return expiry != null && System.currentTimeMillis() < expiry
    }
}