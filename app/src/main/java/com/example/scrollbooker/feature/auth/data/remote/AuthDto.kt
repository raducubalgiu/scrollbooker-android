package com.example.scrollbooker.feature.auth.data.remote

import com.google.gson.annotations.SerializedName

object AuthDto {
    data class LoginRequestDto(
        @SerializedName("username")
        val username: String,

        @SerializedName("password")
        val password: String
    )
    data class LoginResponseDto(
        @SerializedName("access_token")
        val accessToken: String,

        @SerializedName("refresh_token")
        val refreshToken: String,

        @SerializedName("userId")
        val userId: Int,

        @SerializedName("businessId")
        val businessId: Int
    )
    data class RefreshRequestDto(
        @SerializedName("refresh_token")
        val refreshToken: String
    )
}