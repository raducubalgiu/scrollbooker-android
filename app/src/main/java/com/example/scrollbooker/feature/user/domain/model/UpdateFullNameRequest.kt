package com.example.scrollbooker.feature.user.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateFullNameRequest(
    val fullname: String
)