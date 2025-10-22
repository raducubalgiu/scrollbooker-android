package com.example.scrollbooker.entity.booking.business.data.remote
import com.google.gson.annotations.SerializedName

data class RecommendedBusinessDto(
    val user: RecommendedBusinessUserDto,
    val distance: Float,

    @SerializedName("is_open")
    val isOpen: Boolean
)

data class RecommendedBusinessUserDto(
    val id: Int,

    @SerializedName("fullname")
    val fullName: String,

    val username: String,
    val avatar: String?,
    val profession: String,

    @SerializedName("ratings_average")
    val ratingsAverage: Float
)