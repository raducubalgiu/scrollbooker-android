package com.example.scrollbooker.entity.auth.domain.useCase

import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.user.userInfo.domain.useCase.GetUserInfoUseCase
import com.example.scrollbooker.entity.user.userPermissions.domain.useCase.GetUserPermissionsUseCase
import com.example.scrollbooker.store.AuthDataStore
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject

class SaveUserSessionUseCase @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getUserPermissionsUseCase: GetUserPermissionsUseCase
) {
    suspend operator fun invoke(): Result<AuthState> =
        runSuspendCatching {
            val accessToken = authDataStore.getAccessToken().firstOrNull().orEmpty()
            val refreshToken = authDataStore.getRefreshToken().firstOrNull().orEmpty()

            val userInfo = getUserInfoUseCase()
            val userPermissions = getUserPermissionsUseCase()

            authDataStore.storeUserSession(
                accessToken = accessToken,
                refreshToken = refreshToken,
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
        }.onFailure { e ->
            Timber.tag("Save User Session").e(e, "ERROR: on saving user session")
        }
}