package com.example.scrollbooker.entity.user.userProfile.domain.usecase
import com.example.scrollbooker.entity.user.userProfile.domain.repository.UserProfileRepository
import javax.inject.Inject

class SearchUsersClientsUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
//    suspend operator fun invoke(search: String): FeatureState<List<UserSocial>> {
//        return try {
//            val response = repository.searchUsersClients(search)
//            FeatureState.Success(response)
//        } catch (e: Exception) {
//            Timber.tag("Search Users").e("ERROR: on Searching users clients $e")
//            FeatureState.Error(e)
//        }
//    }
}