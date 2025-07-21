package com.example.scrollbooker.entity.booking.business.domain.model

import com.example.scrollbooker.entity.auth.domain.model.AuthState

data class BusinessCreateResponse(
    val authState: AuthState,
    val businessId: Int,
)