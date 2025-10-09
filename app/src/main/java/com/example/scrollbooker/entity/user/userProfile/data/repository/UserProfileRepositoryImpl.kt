package com.example.scrollbooker.entity.user.userProfile.data.repository
import com.example.scrollbooker.entity.auth.data.mappers.toDomain
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.user.userProfile.data.mappers.toDomain
import com.example.scrollbooker.entity.user.userProfile.data.remote.UserProfileApiService
import com.example.scrollbooker.entity.user.userProfile.domain.model.SearchUsernameResponse
import com.example.scrollbooker.entity.user.userProfile.data.remote.UpdateBioRequest
import com.example.scrollbooker.entity.user.userProfile.data.remote.UpdateBirthDateRequest
import com.example.scrollbooker.entity.user.userProfile.data.remote.UpdateFullNameRequest
import com.example.scrollbooker.entity.user.userProfile.data.remote.UpdateGenderRequest
import com.example.scrollbooker.entity.user.userProfile.data.remote.UpdatePublicEmailRequest
import com.example.scrollbooker.entity.user.userProfile.data.remote.UpdateUsernameRequest
import com.example.scrollbooker.entity.user.userProfile.data.remote.UpdateWebsiteRequest
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.entity.user.userProfile.domain.repository.UserProfileRepository
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
    private val apiService: UserProfileApiService
): UserProfileRepository {
    override suspend fun getUserProfile(userId: Int): UserProfile {
        return apiService.getUserProfile(userId).toDomain()
    }

    override suspend fun updateFullName(fullName: String) {
        return apiService.updateFullName(UpdateFullNameRequest(fullname = fullName))
    }

    override suspend fun updateUsername(username: String) {
        return apiService.updateUsername(UpdateUsernameRequest(username))
    }

    override suspend fun updateBirthDate(birthdate: String?): AuthState {
        return apiService.updateBirthDate(UpdateBirthDateRequest(birthdate)).toDomain()
    }

    override suspend fun updateGender(gender: String): AuthState {
        return apiService.updateGender(UpdateGenderRequest(gender)).toDomain()
    }

    override suspend fun updateWebsite(website: String) {
        return apiService.updateWebsite(UpdateWebsiteRequest(website))
    }

    override suspend fun updatePublicEmail(publicEmail: String) {
        return apiService.updatePublicEmail(UpdatePublicEmailRequest(publicEmail))
    }

    override suspend fun updateBio(bio: String) {
        return apiService.updateBio(UpdateBioRequest(bio))
    }

    override suspend fun searchUsername(username: String): SearchUsernameResponse {
        return apiService.searchUsername(username)
    }
}