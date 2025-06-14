package com.example.scrollbooker.feature.profile.data.repository

import com.example.scrollbooker.feature.profile.data.mappers.toDomain
import com.example.scrollbooker.feature.profile.data.remote.UserProfileApiService
import com.example.scrollbooker.feature.profile.domain.model.UpdateBioRequest
import com.example.scrollbooker.feature.profile.domain.model.UpdateFullNameRequest
import com.example.scrollbooker.feature.profile.domain.model.UpdateGenderRequest
import com.example.scrollbooker.feature.profile.domain.model.UpdateUsernameRequest
import com.example.scrollbooker.feature.profile.domain.model.UserProfile
import com.example.scrollbooker.feature.profile.domain.repository.UserProfileRepository
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
    private val apiService: UserProfileApiService
): UserProfileRepository {
    override suspend fun getUserProfile(userId: Int): UserProfile {
        return apiService.getUserProfile(userId).toDomain()
    }

    override suspend fun updateFullName(fullName: String) {
        return apiService.updateFullName(UpdateFullNameRequest(
            fullname = fullName
        ))
    }

    override suspend fun updateUsername(username: String) {
        return apiService.updateUsername(UpdateUsernameRequest(username))
    }

    override suspend fun updateBio(bio: String) {
        return apiService.updateBio(UpdateBioRequest(bio))
    }

    override suspend fun updateGender(gender: String) {
        return apiService.updateGender(UpdateGenderRequest(gender))
    }
//
//    override suspend fun searchUsersClients(q: String): List<UserSocial> {
//        return userApiService.searchUsersClients(q).map { it.toDomain() }
//    }

}