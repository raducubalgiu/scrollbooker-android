package com.example.scrollbooker.entity.auth.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.auth.data.remote.RoleNameEnum
import com.example.scrollbooker.entity.auth.domain.repository.AuthRepository
import timber.log.Timber
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        roleName: RoleNameEnum,
    ): FeatureState<Unit> {
        return try {
            repository.register(email, password, roleName)
            FeatureState.Success(Unit)
        } catch (e: Exception) {
            Timber.tag("Register").e(e, "ERROR: on Register User")
            FeatureState.Error(e)
        }
    }
}