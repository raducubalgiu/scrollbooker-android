package com.example.scrollbooker.entity.auth.data.remote
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthApiService {
    @Multipart
    @POST("auth/login")
    suspend fun login(
        @Part("username") username: RequestBody,
        @Part("password") password: RequestBody,
    ): AuthResponseDto

    @POST("auth/register")
    suspend fun register(@Body request: AuthRequestDto.RegisterDto): AuthResponseDto

    @POST("auth/refresh")
    suspend fun refresh(@Body request: AuthRequestDto.RefreshRequestDto): AuthResponseDto
}