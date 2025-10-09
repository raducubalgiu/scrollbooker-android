package com.example.scrollbooker.entity.user.userProfile.data.remote
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateFullNameRequest(
    val fullname: String
)

@Serializable
data class UpdateUsernameRequest(
    val username: String
)

@Serializable
data class UpdateBirthDateRequest(
    val birthdate: String?
)

@Serializable
data class UpdateBioRequest(
    val bio: String
)

@Serializable
data class UpdateGenderRequest(
    val gender: String
)

@Serializable
data class UpdateWebsiteRequest(
    val website: String
)

data class UpdatePublicEmailRequest(
    @SerializedName("public_email")
    val publicEmail: String
)

