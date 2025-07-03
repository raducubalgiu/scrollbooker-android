package com.example.scrollbooker.entity.business.data.remote

import com.google.gson.annotations.SerializedName

data class BusinessAddressDto(
    val description: String,

    @SerializedName("place_id")
    val placeId: String
)