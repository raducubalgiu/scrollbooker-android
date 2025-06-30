package com.example.scrollbooker.entity.user.userEmailVerify.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.user.userEmailVerify.domain.repository.UserEmailVerifyRepository
import timber.log.Timber
import javax.inject.Inject

class VerifyUserEmailUseCase @Inject constructor(
    private val repository: UserEmailVerifyRepository
) {
    suspend operator fun invoke(): FeatureState<Unit> {
        return try {
            repository.verifyUserEmail()
            FeatureState.Success(Unit)
        } catch (e: Exception) {
            Timber.Forest.tag("Verify User Email").e("ERROR: on Verifying User Email: $e")
            FeatureState.Error(e)
        }
    }
}