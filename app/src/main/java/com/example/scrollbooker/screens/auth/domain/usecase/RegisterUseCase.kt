package com.example.scrollbooker.screens.auth.domain.usecase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.screens.auth.domain.repository.AuthRepository
import timber.log.Timber
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        username: String,
        password: String,
        roleName: String,
        isValidated: Boolean
    ): FeatureState<Unit> {
        return try {
            repository.register(email, username, password, roleName, isValidated)
            FeatureState.Success(Unit)
        } catch (e: Exception) {
            Timber.tag("Register").e(e, "ERROR: on Register User")
            FeatureState.Error(e)
        }
    }
}