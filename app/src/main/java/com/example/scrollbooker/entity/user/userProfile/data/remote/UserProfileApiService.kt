package com.example.scrollbooker.entity.user.userProfile.data.remote
import com.example.scrollbooker.entity.auth.data.remote.AuthStateDto
import com.example.scrollbooker.entity.user.userProfile.domain.model.SearchUsernameResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

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

    @PATCH("users/user-info/birthdate")
    suspend fun updateBirthDate(
        @Body request: UpdateBirthDateRequest
    ): AuthStateDto

    @PATCH("users/user-info/gender")
    suspend fun updateGender(
        @Body request: UpdateGenderRequest
    ): AuthStateDto

    @PATCH("users/user-info/bio")
    suspend fun updateBio(
        @Body request: UpdateBioRequest
    )

    @PATCH("users/user-info/website")
    suspend fun updateWebsite(
        @Body request: UpdateWebsiteRequest
    )

    @PATCH("users/user-info/public-email")
    suspend fun updatePublicEmail(
        @Body request: UpdatePublicEmailRequest
    )

    @GET("users/available-username")
    suspend fun searchUsername(
        @Query("username") username: String
    ): SearchUsernameResponse
}