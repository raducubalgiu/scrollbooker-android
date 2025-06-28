package com.example.scrollbooker.entity.auth.domain.useCase

import com.example.scrollbooker.core.network.tokenProvider.TokenProvider
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.auth.data.remote.RoleNameEnum
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.auth.domain.repository.AuthRepository
import  com.example.scrollbooker.entity.user.userInfo.domain.useCase.GetUserInfoUseCase
import com.example.scrollbooker.entity.user.userPermissions.domain.useCase.GetUserPermissionsUseCase
import com.example.scrollbooker.store.AuthDataStore

import timber.log.Timber
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val tokenProvider: TokenProvider,
    private val authDataStore: AuthDataStore,
    private val repository: AuthRepository,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getUserPermissionsUseCase: GetUserPermissionsUseCase
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        roleName: RoleNameEnum,
    ):  FeatureState<AuthState> {
        return try {
            val response = repository.register(email, password, roleName)
            val loginResponse = repository.login(response.username, response.password)

            tokenProvider.updateTokens(
                accessToken = loginResponse.accessToken,
                refreshToken = loginResponse.refreshToken
            )

            val userInfo = getUserInfoUseCase()
            val userPermissions = getUserPermissionsUseCase()

            authDataStore.storeUserSession(
                accessToken = loginResponse.accessToken,
                refreshToken = loginResponse.refreshToken,
                userId = userInfo.id,
                businessId = userInfo.businessId,
                businessTypeId = userInfo.businessTypeId,
                permissions = userPermissions,
                isValidated = userInfo.isValidated,
                registrationStep = userInfo.registrationStep
            )
            FeatureState.Success(
                AuthState(
                    isValidated = userInfo.isValidated,
                    registrationStep = userInfo.registrationStep
                )
            )
        } catch (e: Exception) {
            Timber.tag("Register").e(e, "ERROR: on Register User")
            FeatureState.Error(e)
        }
    }
}