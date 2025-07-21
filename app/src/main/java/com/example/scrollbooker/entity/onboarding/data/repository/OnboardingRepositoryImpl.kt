package com.example.scrollbooker.entity.onboarding.data.repository

import com.example.scrollbooker.entity.auth.data.mappers.toDomain
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.onboarding.data.remote.OnboardingApiService
import com.example.scrollbooker.entity.onboarding.domain.repository.OnboardingRepository
import com.example.scrollbooker.entity.user.userProfile.domain.model.UpdateBirthDateRequest
import com.example.scrollbooker.entity.user.userProfile.domain.model.UpdateUsernameRequest
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val apiService: OnboardingApiService
): OnboardingRepository {
    override suspend fun collectUserUsername(username: String): AuthState {
        val request = UpdateUsernameRequest(username)
        return apiService.collectUserUsername(request).toDomain()
    }

    override suspend fun collectClientBirthDate(birthdate: String?): AuthState {
        val request = UpdateBirthDateRequest(birthdate)
        return apiService.collectClientBirthDate(request).toDomain()
    }
}