package com.example.scrollbooker.shared.user.userSocial.domain.model

data class UserSocial(
    val id: Int,
    val fullName: String,
    val username: String,
    val avatar: String?,
    val isFollow: Boolean,
    val profession: String?
)