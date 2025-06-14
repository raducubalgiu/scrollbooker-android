package com.example.scrollbooker.feature.auth.data.remote

import com.google.gson.annotations.SerializedName

data class UserInfoDto(
    val id: Int,

    @SerializedName("business_id")
    val businessId: Int,

    @SerializedName("business_type_id")
    val businessTypeId: Int
)