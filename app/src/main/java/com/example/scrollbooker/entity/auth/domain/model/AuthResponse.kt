package com.example.scrollbooker.entity.auth.domain.model

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String
)