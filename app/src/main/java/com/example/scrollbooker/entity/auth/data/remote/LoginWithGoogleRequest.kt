package com.example.scrollbooker.entity.auth.data.remote

import com.google.gson.annotations.SerializedName

data class LoginWithGoogleRequest(
    @SerializedName("id_token")
    val idToken: String,

    @SerializedName("role_name")
    val roleName: String
)