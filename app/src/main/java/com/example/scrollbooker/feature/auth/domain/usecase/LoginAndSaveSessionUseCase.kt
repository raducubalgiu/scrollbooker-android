package com.example.scrollbooker.feature.auth.domain.usecase
import com.example.scrollbooker.core.network.tokenProvider.TokenProvider
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.feature.auth.domain.repository.AuthRepository
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
    suspend operator fun invoke(username: String, password: String): FeatureState<Unit> {
        return try {
            val loginResponse = repository.login(username, password)

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
                permissions = userPermissions
            )
            FeatureState.Success(Unit)
        } catch (e: Exception) {
            Timber.tag("Login and Save Session").e(e, "ERROR: on Fetching or Saving User Session")
            FeatureState.Error(e)
        }
    }
}
