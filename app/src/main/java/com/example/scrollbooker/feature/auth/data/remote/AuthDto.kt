package com.example.scrollbooker.feature.auth.data.remote

object AuthDto {
    data class LoginRequestDto(val username: String, val password: String)
    data class LoginResponseDto(val accessToken: String, val refreshToken: String, val userId: Int, val businessId: Int)
}