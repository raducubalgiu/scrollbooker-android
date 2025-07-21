package com.example.scrollbooker.entity.onboarding.domain.repository
import com.example.scrollbooker.entity.auth.domain.model.AuthState

interface OnboardingRepository {
    suspend fun collectUserUsername(username: String): AuthState
}