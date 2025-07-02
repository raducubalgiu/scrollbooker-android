package com.example.scrollbooker.entity.user.userProfile.data.remote
import com.example.scrollbooker.entity.auth.data.remote.AuthStateDto
import com.example.scrollbooker.entity.user.userProfile.domain.model.SearchUsernameResponse
import com.example.scrollbooker.entity.user.userProfile.domain.model.UpdateBioRequest
import com.example.scrollbooker.entity.user.userProfile.domain.model.UpdateBirthDateRequest
import com.example.scrollbooker.entity.user.userProfile.domain.model.UpdateFullNameRequest
import com.example.scrollbooker.entity.user.userProfile.domain.model.UpdateGenderRequest
import com.example.scrollbooker.entity.user.userProfile.domain.model.UpdateUsernameRequest
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
    ): AuthStateDto

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

    @GET("users/available-username")
    suspend fun searchUsername(
        @Query("username") username: String
    ): SearchUsernameResponse
}