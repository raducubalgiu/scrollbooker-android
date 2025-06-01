package com.example.scrollbooker.feature.auth.data.repository
import com.example.scrollbooker.core.network.tokenProvider.TokenProvider
import com.example.scrollbooker.core.network.util.decodeJwtExpiry
import com.example.scrollbooker.feature.auth.data.remote.AuthApiService
import com.example.scrollbooker.feature.auth.data.remote.AuthDto
import com.example.scrollbooker.feature.auth.domain.model.LoginRequest
import com.example.scrollbooker.feature.auth.domain.model.LoginResponse
import com.example.scrollbooker.feature.auth.domain.repository.AuthRepository
import com.example.scrollbooker.feature.auth.domain.usecase.GetLoginUseCase
import com.example.scrollbooker.feature.auth.domain.usecase.GetUserInfoUseCase
import com.example.scrollbooker.feature.auth.domain.usecase.GetUserPermissionsUseCase
import com.example.scrollbooker.store.AuthDataStore
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApiService,
    private val tokenProvider: TokenProvider,
    private val authDataStore: AuthDataStore,
    private val getLoginInfoUseCase: GetLoginUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getUserPermissionsUseCase: GetUserPermissionsUseCase
): AuthRepository {
    override suspend fun loginAndSaveUserSession(request: LoginRequest): Result<LoginResponse> {
        return try {
            val loginInfo = getLoginInfoUseCase(request.username, request.password)

            tokenProvider.updateTokens(
                accessToken = loginInfo.accessToken,
                refreshToken = loginInfo.refreshToken
            )

            val userInfo = getUserInfoUseCase()
            val userPermissions = getUserPermissionsUseCase()

            authDataStore.storeUserSession(
                accessToken = loginInfo.accessToken,
                refreshToken = loginInfo.refreshToken,
                userId = userInfo.id,
                businessId = userInfo.businessId,
                permissions = userPermissions
            )

            Result.success(
                LoginResponse(
                    accessToken = loginInfo.accessToken,
                    refreshToken = loginInfo.refreshToken,
                    userId = userInfo.id,
                    businessId = userInfo.businessId
                )
            )
        } catch (e: Exception) {
            Timber.tag("Login").e(e, "ERROR: on Fetching or Saving User Session")
            Result.failure(e)
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        val accessToken = authDataStore.getAccessToken().firstOrNull()
        val refreshToken = authDataStore.getRefreshToken().firstOrNull()

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