package com.example.scrollbooker.entity.auth.domain.useCase
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.auth.domain.repository.AuthRepository
import timber.log.Timber
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val saveSessionUseCase: SaveSessionUseCase
) {
    suspend operator fun invoke(username: String, password: String): FeatureState<AuthState> {
        return try {
            val loginResponse = repository.login(username, password)
            return saveSessionUseCase(loginResponse)
        } catch (e: Exception) {
            Timber.tag("Login").e(e, "ERROR: on Login User")
            FeatureState.Error(e)
        }
    }
}
