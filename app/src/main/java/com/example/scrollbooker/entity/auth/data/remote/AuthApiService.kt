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
    ): AuthDto.LoginResponseDto

    @POST("auth/register")
    suspend fun register(@Body request: AuthDto.RegisterDto): AuthDto.LoginResponseDto

    @POST("auth/refresh")
    suspend fun refresh(@Body request: AuthDto.RefreshRequestDto): AuthDto.LoginResponseDto
}