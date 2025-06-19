package com.example.scrollbooker.shared.userProfile.domain.model

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
data class UpdateBioRequest(
    val bio: String
)

@Serializable
data class UpdateGenderRequest(
    val gender: String
)

