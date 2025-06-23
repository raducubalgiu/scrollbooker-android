package com.example.scrollbooker.shared.user.userProfile.data.remote
import com.example.scrollbooker.shared.user.userProfile.domain.model.UpdateBioRequest
import com.example.scrollbooker.shared.user.userProfile.domain.model.UpdateFullNameRequest
import com.example.scrollbooker.shared.user.userProfile.domain.model.UpdateGenderRequest
import com.example.scrollbooker.shared.user.userProfile.domain.model.UpdateUsernameRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface UserProfileApiService {
    @GET("users/{userId}/user-profile")
    suspend fun getUserProfile(
        @Path("userId") userId: Int
    ): UserProfileDto

    @PATCH("users/user-info/fullname")
    suspend fun updateFullName(
        @Body request: UpdateFullNameRequest
    )

    @PATCH("users/user-info/username")
    suspend fun updateUsername(
        @Body request: UpdateUsernameRequest
    )

    @PATCH("users/user-info/bio")
    suspend fun updateBio(
        @Body request: UpdateBioRequest
    )

    @PATCH("users/user-info/gender")
    suspend fun updateGender(
        @Body request: UpdateGenderRequest
    )
}