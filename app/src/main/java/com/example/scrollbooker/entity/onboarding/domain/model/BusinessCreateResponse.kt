package com.example.scrollbooker.entity.onboarding.domain.model

import com.example.scrollbooker.entity.auth.domain.model.AuthState

data class BusinessCreateResponse(
    val businessId: Int,
    val businessTypeId: Int,
    val onboardingState: AuthState
)