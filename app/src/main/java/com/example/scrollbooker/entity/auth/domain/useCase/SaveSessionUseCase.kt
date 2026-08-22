package com.example.scrollbooker.entity.auth.domain.useCase

import com.example.scrollbooker.core.network.tokenProvider.TokenProvider
import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.auth.domain.model.AuthResponse
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.user.userInfo.domain.useCase.GetUserInfoUseCase
import com.example.scrollbooker.entity.user.userPermissions.domain.useCase.GetUserPermissionsUseCase
import com.example.scrollbooker.store.AuthDataStore
import javax.inject.Inject

class SaveSessionUseCase @Inject constructor(
    private val tokenProvider: TokenProvider,
    private val authDataStore: AuthDataStore,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getUserPermissionsUseCase: GetUserPermissionsUseCase
) {
    suspend operator fun invoke(authResponse: AuthResponse): Result<AuthState> =
        runSuspendCatching {
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
                username = userInfo.username,
                fullName = userInfo.fullname,
                businessId = userInfo.businessId,
                businessOwnerId = userInfo.businessOwnerId,
                businessTypeId = userInfo.businessTypeId,
                hasEmployees = userInfo.hasEmployees,
                permissions = userPermissions
            )

            AuthState(
                isValidated = userInfo.isValidated,
                registrationStep = userInfo.registrationStep
            )
        }
}