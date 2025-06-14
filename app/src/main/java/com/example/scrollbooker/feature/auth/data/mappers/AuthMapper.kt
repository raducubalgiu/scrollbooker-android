package com.example.scrollbooker.feature.auth.data.mappers

import com.example.scrollbooker.feature.auth.data.remote.auth.AuthDto
import com.example.scrollbooker.feature.auth.domain.model.LoginResponse

fun AuthDto.LoginResponseDto.toDoman(): LoginResponse {
    return LoginResponse(
        accessToken = accessToken,
        refreshToken = refreshToken,
        userId = userId,
        businessId = businessId
    )
}