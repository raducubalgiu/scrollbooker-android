package com.example.scrollbooker.entity.onboarding.data.remote

import com.google.gson.annotations.SerializedName

data class BusinessCreateRequest(
    val description: String?,

    @SerializedName("place_id")
    val placeId: String,

    @SerializedName("business_type_id")
    val businessTypeId: Int,

    @SerializedName("owner_fullname")
    val ownerFullName: String
)