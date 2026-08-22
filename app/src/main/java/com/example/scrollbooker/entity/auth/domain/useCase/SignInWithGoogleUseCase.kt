package com.example.scrollbooker.entity.auth.domain.useCase
import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.auth.data.remote.RoleNameEnum
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.auth.domain.repository.AuthRepository
import timber.log.Timber
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val saveSessionUseCase: SaveSessionUseCase
) {
    suspend operator fun invoke(idToken: String, roleName: RoleNameEnum): Result<AuthState> =
        runSuspendCatching {
            val response = repository.signInWithGoogle(idToken, roleName)
            saveSessionUseCase(response).getOrThrow()
        }.onFailure { e ->
            Timber.tag("Sign in with Google").e(e, "ERROR: on Sign In With Google")
        }
}