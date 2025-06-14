package com.example.scrollbooker.feature.auth.domain.usecase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.feature.auth.domain.repository.AuthRepository
import timber.log.Timber
import javax.inject.Inject

class IsLoggedInUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): FeatureState<Unit> {
        return try {
            val isLoggedIn = repository.isLoggedIn()

            if(isLoggedIn) {
                FeatureState.Success(Unit)
            } else {
                Timber.tag("Is Logged In").e("ERROR: User Is Not Logged In")
                FeatureState.Error()
            }

        } catch (e: Exception) {
            Timber.tag("Is Logged In").e(e, "ERROR: on Trying to get loggedIn Status")
            FeatureState.Error()
        }
    }
}