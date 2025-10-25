package com.example.scrollbooker.entity.booking.business.data.remote

import com.google.gson.annotations.SerializedName

data class BusinessLocationDto(
    val distance: Float?,
    val address: String,

    @SerializedName("map_url")
    val mapUrl: String
)