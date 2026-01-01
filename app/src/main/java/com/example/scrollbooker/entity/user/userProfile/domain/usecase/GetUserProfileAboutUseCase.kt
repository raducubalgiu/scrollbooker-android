package com.example.scrollbooker.entity.user.userProfile.domain.usecase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfileAbout
import com.example.scrollbooker.entity.user.userProfile.domain.repository.UserProfileRepository
import com.example.scrollbooker.store.AuthDataStore
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject

class GetUserProfileAboutUseCase @Inject constructor(
    private val repository: UserProfileRepository,
    private val authDataStore: AuthDataStore
) {
    suspend operator fun invoke(): FeatureState<UserProfileAbout> {
        val userId = authDataStore.getUserId().firstOrNull()

        if(userId == null) {
            Timber.tag("UserProfile").e("ERROR: on Fetching User Profile. User Id Not Found")
            return FeatureState.Error()
        }

        return try {
            val response = repository.getUserProfileAbout(userId)
            FeatureState.Success(response)
        } catch (e: Exception) {
            Timber.tag("User Profile About").e(e, "ERROR: on Fetching User Profile About")
            FeatureState.Error(e)
        }
    }
}