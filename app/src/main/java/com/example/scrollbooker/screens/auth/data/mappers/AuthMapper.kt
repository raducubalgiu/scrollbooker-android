package com.example.scrollbooker.screens.auth.data.mappers

import com.example.scrollbooker.screens.auth.data.remote.auth.AuthDto
import com.example.scrollbooker.screens.auth.domain.model.LoginResponse

fun AuthDto.LoginResponseDto.toDoman(): LoginResponse {
    return LoginResponse(
        accessToken = accessToken,
        refreshToken = refreshToken,
        userId = userId,
        businessId = businessId
    )
}