package com.example.scrollbooker.entity.user.userSocial.data.remote

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class UserSocialDto(
    val id: Int,

    @SerializedName("fullname")
    val fullName: String,

    val username: String,
    val avatar: String?,

    @SerializedName("is_follow")
    val isFollow: Boolean,

    val profession: String?,

    @SerializedName("ratings_average")
    val ratingsAverage: Float?,

    @SerializedName("is_business_or_employee")
    val isBusinessOrEmployee: Boolean?,
)