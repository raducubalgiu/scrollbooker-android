package com.example.scrollbooker.entity.auth.data.remote

import com.google.gson.annotations.SerializedName

data class AuthResponseDto(
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("refresh_token")
    val refreshToken: String,

    @SerializedName("token_type")
    val tokenType: String
)