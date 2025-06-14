package com.example.scrollbooker.feature.userSocial.data.remote

import com.google.gson.annotations.SerializedName

data class UserSocialDto(
    val id: Int,

    @SerializedName("fullname")
    val fullName: String,

    val username: String,
    val avatar: String?,

    @SerializedName("is_follow")
    val isFollow: Boolean
)