package com.example.scrollbooker.entity.booking.business.data.remote

import com.google.gson.annotations.SerializedName

data class BusinessSummaryDto(
    val id: Int,

    @SerializedName("fullname")
    val fullName: String,

    val username: String,
    val avatar: String?,

    @SerializedName("ratings_average")
    val ratingsAverage: Float,

    @SerializedName("ratings_count")
    val ratingsCount: Int,

    val profession: String
)