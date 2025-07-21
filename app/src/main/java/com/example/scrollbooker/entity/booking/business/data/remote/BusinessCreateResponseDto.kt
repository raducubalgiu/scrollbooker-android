package com.example.scrollbooker.entity.booking.business.data.remote

import com.example.scrollbooker.entity.auth.data.remote.AuthStateDto
import com.google.gson.annotations.SerializedName

data class BusinessCreateResponseDto(
    val authState: AuthStateDto,

    @SerializedName("business_id")
    val businessId: Int
)