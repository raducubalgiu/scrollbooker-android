package com.example.scrollbooker.shared.businessType.data.remote

import com.google.gson.annotations.SerializedName

data class BusinessTypeDto(
    val id: Int,
    val name: String,

    @SerializedName("business_domain_id")
    val businessDomainId: Int
)