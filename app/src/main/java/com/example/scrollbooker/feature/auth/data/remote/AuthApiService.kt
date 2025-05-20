package com.example.scrollbooker.feature.auth.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("login")
    suspend fun login(@Body request: AuthDto.LoginRequestDto): AuthDto.LoginResponseDto
}