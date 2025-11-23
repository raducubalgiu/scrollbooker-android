package com.example.scrollbooker.entity.user.userProfile.domain.usecase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.entity.user.userProfile.domain.repository.UserProfileRepository
import timber.log.Timber
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(userId: Int?, lat: Float?, lng: Float?): FeatureState<UserProfile> {
        if(userId == null) {
            Timber.tag("UserProfile").e("ERROR: on Fetching User Profile. User Id Not Found")
            return FeatureState.Error()
        }

        return try {
            val response = repository.getUserProfile(userId, lat, lng)
            FeatureState.Success(response)
        } catch (e: Exception) {
            Timber.tag("UserProfile").e("ERROR: on Fetching User Profile: $e")
            FeatureState.Error(e)
        }
    }
}