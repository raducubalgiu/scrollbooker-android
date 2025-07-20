package com.example.scrollbooker.entity.search.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.search.domain.repository.SearchRepository
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import timber.log.Timber
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(query: String): FeatureState<List<UserSocial>> {
        return try {
            val response = repository.searchUsers(query, roleClient = true)
            FeatureState.Success(response)
        } catch (e: Exception) {
            Timber.tag("Search Users Clients").e("ERROR: on Searching users as clients $e")
            FeatureState.Error(e)
        }
    }
}