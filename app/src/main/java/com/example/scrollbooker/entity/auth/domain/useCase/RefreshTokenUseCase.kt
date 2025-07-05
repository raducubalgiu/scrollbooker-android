package com.example.scrollbooker.entity.auth.domain.useCase

import com.example.scrollbooker.core.network.tokenProvider.TokenProvider
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.auth.data.remote.AuthApiService
import com.example.scrollbooker.entity.auth.data.remote.AuthDto
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.user.userInfo.domain.useCase.GetUserInfoUseCase
import com.example.scrollbooker.store.AuthDataStore
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val apiService: AuthApiService,
    private val authDataStore: AuthDataStore,
    private val tokenProvider: TokenProvider,
    private val getUserInfoUseCase: GetUserInfoUseCase
) {
    suspend operator fun invoke(refreshToken: String): FeatureState<AuthState> {
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

            return FeatureState.Error()
        }
    }
}