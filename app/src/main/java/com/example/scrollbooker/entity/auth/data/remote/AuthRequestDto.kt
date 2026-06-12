package com.example.scrollbooker.entity.auth.data.remote

import com.google.gson.annotations.SerializedName

object AuthRequestDto {
    data class RegisterDto(
        val email: String,
        val password: String,

        @SerializedName("role_name")
        val roleName: RoleNameEnum,
    )

    data class RefreshRequestDto(
        @SerializedName("refresh_token")
        val refreshToken: String
    )
}