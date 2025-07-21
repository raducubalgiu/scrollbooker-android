package com.example.scrollbooker.entity.onboarding.data.remote

import com.example.scrollbooker.entity.auth.data.remote.AuthStateDto
import com.example.scrollbooker.entity.user.userProfile.domain.model.UpdateUsernameRequest
import retrofit2.http.Body
import retrofit2.http.PATCH

interface OnboardingApiService {
    @PATCH("/onboarding/collect-user-username")
    suspend fun collectUserUsername(
        @Body request: UpdateUsernameRequest
    ): AuthStateDto
}