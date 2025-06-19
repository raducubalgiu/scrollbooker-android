package com.example.scrollbooker.shared.userProfile.domain.usecase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.shared.userProfile.domain.model.UserProfile
import com.example.scrollbooker.shared.userProfile.domain.repository.UserProfileRepository
import kotlinx.coroutines.delay
import timber.log.Timber
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(userId: Int?): FeatureState<UserProfile> {
        if(userId == null) {
            Timber.tag("UserProfile").e("ERROR: on Fetching User Profile. User Id Not Found")
            return FeatureState.Error()
        }

        return try {
            delay(200)
            val response = repository.getUserProfile(userId)
            FeatureState.Success(response)
        } catch (e: Exception) {
            Timber.tag("UserProfile").e("ERROR: on Fetching User Profile: $e")
            FeatureState.Error(e)
        }
    }
}