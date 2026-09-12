package com.example.scrollbooker.entity.booking.businessClient.data.remote

import com.google.gson.annotations.SerializedName

data class BusinessClientDto(
    val id: Int,

    @SerializedName("business_id")
    val businessId: Int,

    @SerializedName("user_id")
    val userId: Int?,

    val fullname: String,
    val phone: String?,
)

data class BusinessClientCreateDto(
    val fullname: String,
    val phone: String?,
)
