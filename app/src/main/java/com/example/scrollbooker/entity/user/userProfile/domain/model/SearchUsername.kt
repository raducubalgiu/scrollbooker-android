package com.example.scrollbooker.entity.user.userProfile.domain.model

data class SearchUsernameResponse(
    val available: Boolean,
    val suggestions: List<String>,
    val username: String?
)

