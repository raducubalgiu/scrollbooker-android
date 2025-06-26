package com.example.scrollbooker.entity.auth.data.mappers

import com.example.scrollbooker.entity.auth.data.remote.AuthDto
import com.example.scrollbooker.entity.auth.domain.model.LoginResponse

fun AuthDto.LoginResponseDto.toDoman(): LoginResponse {
    return LoginResponse(
        accessToken = accessToken,
        refreshToken = refreshToken,
        userId = userId,
        businessId = businessId
    )
}