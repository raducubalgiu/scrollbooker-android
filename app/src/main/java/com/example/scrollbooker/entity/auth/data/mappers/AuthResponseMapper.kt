package com.example.scrollbooker.entity.auth.data.mappers

import com.example.scrollbooker.entity.auth.data.remote.AuthResponseDto
import com.example.scrollbooker.entity.auth.domain.model.AuthResponse

fun AuthResponseDto.toDomain(): AuthResponse {
    return AuthResponse(
        accessToken = accessToken,
        refreshToken = refreshToken,
        tokenType = tokenType
    )
}