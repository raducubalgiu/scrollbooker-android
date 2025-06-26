package com.example.scrollbooker.entity.auth.domain.model

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: Int,
    val businessId: Int?
)
