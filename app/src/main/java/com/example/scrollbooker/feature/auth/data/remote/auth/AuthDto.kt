package com.example.scrollbooker.feature.auth.data.remote.auth

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

    data class RefreshRequestDto(
        @SerializedName("refresh_token")
        val refreshToken: String
    )
}