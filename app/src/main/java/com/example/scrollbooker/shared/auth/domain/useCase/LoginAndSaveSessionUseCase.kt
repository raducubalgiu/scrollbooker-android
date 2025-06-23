package com.example.scrollbooker.shared.auth.domain.useCase
import com.example.scrollbooker.core.network.tokenProvider.TokenProvider
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.shared.auth.domain.model.AuthState
import com.example.scrollbooker.shared.auth.domain.repository.AuthRepository
import com.example.scrollbooker.shared.user.userInfo.domain.useCase.GetUserInfoUseCase
import com.example.scrollbooker.shared.user.userPermissions.domain.useCase.GetUserPermissionsUseCase
import com.example.scrollbooker.store.AuthDataStore
import timber.log.Timber
import javax.inject.Inject

class LoginAndSaveSessionUseCase @Inject constructor(
    private val tokenProvider: TokenProvider,
    private val authDataStore: AuthDataStore,
    private val repository: AuthRepository,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getUserPermissionsUseCase: GetUserPermissionsUseCase
) {
    suspend operator fun invoke(username: String, password: String): FeatureState<AuthState> {
        return try {
            val loginResponse = repository.login(username, password)

            tokenProvider.updateTokens(
                accessToken = loginResponse.accessToken,
                refreshToken = loginResponse.refreshToken
            )

            val userInfo = getUserInfoUseCase()
            val userPermissions = getUserPermissionsUseCase()

            Timber.tag("Login USER INFO").e("USER INFO: $userInfo")

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
            Timber.tag("Login and Save Session").e(e, "ERROR: on Fetching or Saving User Session")
            FeatureState.Error(e)
        }
    }
}
