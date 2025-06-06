package com.example.scrollbooker.feature.userSocial.domain.model

data class UserSocial(
    val id: Int,
    val fullName: String,
    val username: String,
    val avatar: String,
    val isFollow: Boolean
)