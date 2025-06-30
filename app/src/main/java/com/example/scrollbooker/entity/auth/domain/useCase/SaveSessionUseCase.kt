package com.example.scrollbooker.entity.auth.domain.useCase

import com.example.scrollbooker.core.network.tokenProvider.TokenProvider
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.auth.domain.model.LoginResponse
import com.example.scrollbooker.entity.user.userInfo.domain.useCase.GetUserInfoUseCase
import com.example.scrollbooker.entity.user.userPermissions.domain.useCase.GetUserPermissionsUseCase
import com.example.scrollbooker.store.AuthDataStore
import timber.log.Timber
import javax.inject.Inject

class SaveSessionUseCase @Inject constructor(
    private val tokenProvider: TokenProvider,
    private val authDataStore: AuthDataStore,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getUserPermissionsUseCase: GetUserPermissionsUseCase
) {
    suspend operator fun invoke(authResponse: LoginResponse): FeatureState<AuthState> {
        return try {
            tokenProvider.updateTokens(
                accessToken = authResponse.accessToken,
                refreshToken = authResponse.refreshToken
            )

            val userInfo = getUserInfoUseCase()
            val userPermissions = getUserPermissionsUseCase()

            authDataStore.storeUserSession(
                accessToken = authResponse.accessToken,
                refreshToken = authResponse.refreshToken,
                userId = userInfo.id,
                businessId = userInfo.businessId,
                businessTypeId = userInfo.businessTypeId,
                permissions = userPermissions
            )

            FeatureState.Success(
                AuthState(
                    isValidated = userInfo.isValidated,
                    registrationStep = userInfo.registrationStep
                )
            )
        } catch (e: Exception) {
            Timber.tag("Save Session").e(e, "ERROR: on Saving Session")
            FeatureState.Error(e)
        }
    }
}