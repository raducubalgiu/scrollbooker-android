package com.example.scrollbooker.entity.user.userProfile.domain.usecase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfileAbout
import com.example.scrollbooker.entity.user.userProfile.domain.repository.UserProfileRepository
import timber.log.Timber
import javax.inject.Inject

class GetUserProfileAboutUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(userId: Int): FeatureState<UserProfileAbout> {
        return try {
            val response = repository.getUserProfileAbout(userId)
            FeatureState.Success(response)
        } catch (e: Exception) {
            Timber.tag("User Profile About").e(e, "ERROR: on Fetching User Profile About")
            FeatureState.Error(e)
        }
    }
}