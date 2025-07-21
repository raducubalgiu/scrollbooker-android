package com.example.scrollbooker.entity.onboarding.data.remote

import com.example.scrollbooker.entity.auth.data.remote.AuthStateDto
import com.example.scrollbooker.entity.user.userProfile.domain.model.UpdateBirthDateRequest
import com.example.scrollbooker.entity.user.userProfile.domain.model.UpdateGenderRequest
import com.example.scrollbooker.entity.user.userProfile.domain.model.UpdateUsernameRequest
import retrofit2.http.Body
import retrofit2.http.PATCH

interface OnboardingApiService {
    @PATCH("/onboarding/collect-user-username")
    suspend fun collectUserUsername(
        @Body request: UpdateUsernameRequest
    ): AuthStateDto

    @PATCH("/onboarding/collect-client-birthdate")
    suspend fun collectClientBirthDate(
        @Body request: UpdateBirthDateRequest
    ): AuthStateDto

    @PATCH("/onboarding/collect-client-gender")
    suspend fun collectClientGender(
        @Body request: UpdateGenderRequest
    ): AuthStateDto
}