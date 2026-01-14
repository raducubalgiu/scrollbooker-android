package com.example.scrollbooker.entity.booking.business.data.remote

import com.google.gson.annotations.SerializedName

data class BusinessMediaFileDto(
    val url: String,

    @SerializedName("url_key")
    val urlKey: String,

    @SerializedName("thumbnail_url")
    val thumbnailUrl: String,

    @SerializedName("thumbnail_key")
    val thumbnailKey: String,

    val type: String,

    @SerializedName("order_index")
    val orderIndex: Int
)