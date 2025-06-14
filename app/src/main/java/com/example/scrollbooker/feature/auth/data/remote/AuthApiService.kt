package com.example.scrollbooker.feature.auth.data.remote

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthApiService {
    @Multipart
    @POST("auth/login")
    suspend fun getLoginInfo(
        @Part("username") username: RequestBody,
        @Part("password") password: RequestBody,
    ): AuthDto.LoginResponseDto

    @GET("auth/user-permissions/")
    suspend fun getUserPermissions(): List<PermissionDto>

    @POST("auth/refresh")
    suspend fun refresh(@Body request: AuthDto.RefreshRequestDto): AuthDto.LoginResponseDto
}