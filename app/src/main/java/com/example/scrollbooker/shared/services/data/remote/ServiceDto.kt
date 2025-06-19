package com.example.scrollbooker.shared.services.data.remote

import com.google.gson.annotations.SerializedName

data class ServiceDto(
    val id: Int,
    val name: String,

    @SerializedName("business_domain_id")
    val businessDomainId: Int
)