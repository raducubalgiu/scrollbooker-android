package com.example.scrollbooker.entity.onboarding.data.remote

import com.example.scrollbooker.entity.auth.data.remote.AuthStateDto
import com.google.gson.annotations.SerializedName

data class BusinessCreateResponseDto(
    @SerializedName("business_id")
    val businessId: Int,

    @SerializedName("business_type_id")
    val businessTypeId: Int,

    @SerializedName("onboarding_state")
    val onboardingState: AuthStateDto
)