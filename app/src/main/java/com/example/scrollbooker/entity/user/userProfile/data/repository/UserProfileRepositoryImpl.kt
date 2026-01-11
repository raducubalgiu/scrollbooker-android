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
import com.example.scrollbooker.entity.user.userProfile.data.remote.UserAvatarRequest
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfileAbout
import com.example.scrollbooker.entity.user.userProfile.domain.repository.UserProfileRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Multipart
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
    private val apiService: UserProfileApiService
): UserProfileRepository {
    override suspend fun getUserProfile(userId: Int, lat: Float?, lng: Float?): UserProfile {
        return apiService.getUserProfile(userId, lat, lng).toDomain()
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

    override suspend fun updateAvatar(request: UserAvatarRequest) {
        val requestBody = request.bytes.toRequestBody(request.mimeType.toMediaType())
        val part = MultipartBody.Part.createFormData(
            name = "avatar",
            filename = request.fileName,
            body = requestBody
        )
        apiService.updateAvatar(part)
    }

    override suspend fun updateBio(bio: String) {
        return apiService.updateBio(UpdateBioRequest(bio))
    }

    override suspend fun searchUsername(username: String): SearchUsernameResponse {
        return apiService.searchUsername(username)
    }

    override suspend fun getUserProfileAbout(userId: Int): UserProfileAbout {
        return apiService.getUserProfileAbout(userId).toDomain()
    }
}