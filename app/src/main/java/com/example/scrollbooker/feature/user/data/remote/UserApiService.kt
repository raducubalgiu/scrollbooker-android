package com.example.scrollbooker.feature.user.data.remote

import com.example.scrollbooker.feature.auth.data.remote.PermissionDto
import com.example.scrollbooker.feature.user.domain.model.UpdateBioRequest
import com.example.scrollbooker.feature.user.domain.model.UpdateFullNameRequest
import com.example.scrollbooker.feature.user.domain.model.UpdateGenderRequest
import com.example.scrollbooker.feature.user.domain.model.UpdateUsernameRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserApiService {
    @GET("auth/user-info/")
    suspend fun getUserInfo(): UserDto

    @GET("auth/user-permissions/")
    suspend fun getUserPermissions(): List<PermissionDto>

    @PATCH("users/user-info/fullname/")
    suspend fun updateUserFullName(
        @Body request: UpdateFullNameRequest
    )

    @PATCH("users/user-info/username/")
    suspend fun updateUsername(
        @Body request: UpdateUsernameRequest
    )

    @PATCH("users/user-info/bio/")
    suspend fun updateBio(
        @Body request: UpdateBioRequest
    )

    @PATCH("users/user-info/gender/")
    suspend fun updateGender(
        @Body request: UpdateGenderRequest
    )
}