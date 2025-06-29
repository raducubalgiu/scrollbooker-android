package com.example.scrollbooker.entity.user.userProfile.domain.usecase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.user.userProfile.domain.model.SearchUsernameResponse
import com.example.scrollbooker.entity.user.userProfile.domain.repository.UserProfileRepository
import timber.log.Timber
import javax.inject.Inject

class SearchUsernameUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(username: String): FeatureState<SearchUsernameResponse> {
        return try {
            val response = repository.searchUsername(username)
            FeatureState.Success(
                SearchUsernameResponse(
                    available = response.available,
                    suggestions = response.suggestions,
                    username = username
                )
            )
        } catch (e: Exception) {
            Timber.tag("Search Username").e("ERROR: on Searching username $e")
            FeatureState.Error(e)
        }
    }
}