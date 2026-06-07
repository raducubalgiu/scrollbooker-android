package com.example.scrollbooker.entity.search.data.remote

import com.google.gson.annotations.SerializedName

data class SearchUserDto(
    val id: Int,
    val username: String,

    @SerializedName("fullname")
    val fullName: String,
    val avatar: String?,

    @SerializedName("ratings_average")
    val ratingsAverage: Float,

    @SerializedName("is_business_or_employee")
    val isBusinessOrEmployee: Boolean
)