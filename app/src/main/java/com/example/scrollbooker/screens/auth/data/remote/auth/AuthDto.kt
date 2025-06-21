package com.example.scrollbooker.screens.auth.data.remote.auth

import com.google.gson.annotations.SerializedName

object AuthDto {
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

    data class RegisterDto(
        val email: String,
        val username: String,
        val password: String,

        @SerializedName("role_name")
        val roleName: String,

        @SerializedName("is_validated")
        val isValidated: Boolean
    )

    data class RefreshRequestDto(
        @SerializedName("refresh_token")
        val refreshToken: String
    )
}