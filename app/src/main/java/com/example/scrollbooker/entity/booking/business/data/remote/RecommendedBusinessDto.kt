package com.example.scrollbooker.entity.booking.business.data.remote

import com.example.scrollbooker.entity.user.userSocial.data.remote.UserSocialDto
import com.google.gson.annotations.SerializedName

data class RecommendedBusinessDto(
    val user: UserSocialDto,
    val distance: Float,

    @SerializedName("is_open")
    val isOpen: Boolean
)