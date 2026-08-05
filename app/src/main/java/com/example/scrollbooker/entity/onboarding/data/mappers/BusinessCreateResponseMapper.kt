package com.example.scrollbooker.entity.onboarding.data.mappers

import com.example.scrollbooker.entity.auth.data.mappers.toDomain
import com.example.scrollbooker.entity.onboarding.data.remote.BusinessCreateResponseDto
import com.example.scrollbooker.entity.onboarding.domain.model.BusinessCreateResponse

fun BusinessCreateResponseDto.toDomain(): BusinessCreateResponse {
    return BusinessCreateResponse(
        businessId = this.businessId,
        businessTypeId = this.businessTypeId,
        onboardingState = this.onboardingState.toDomain()
    )
}