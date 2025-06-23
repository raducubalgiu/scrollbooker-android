package com.example.scrollbooker.shared.auth.data.mappers

import com.example.scrollbooker.shared.auth.data.remote.AuthDto
import com.example.scrollbooker.shared.auth.domain.model.LoginResponse

fun AuthDto.LoginResponseDto.toDoman(): LoginResponse {
    return LoginResponse(
        accessToken = accessToken,
        refreshToken = refreshToken,
        userId = userId,
        businessId = businessId
    )
}