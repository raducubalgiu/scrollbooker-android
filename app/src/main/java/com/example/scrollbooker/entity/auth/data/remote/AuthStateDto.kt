package com.example.scrollbooker.entity.auth.data.remote
import com.google.gson.annotations.SerializedName

data class AuthStateDto(
    @SerializedName("is_validated")
    val isValidated: Boolean,

    @SerializedName("registration_step")
    val registrationStep: String?
)