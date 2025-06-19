package com.example.scrollbooker.screens.profile.userSocial.domain.model

data class UserSocial(
    val id: Int,
    val fullName: String,
    val username: String,
    val avatar: String?,
    val isFollow: Boolean,
    val profession: String?
)