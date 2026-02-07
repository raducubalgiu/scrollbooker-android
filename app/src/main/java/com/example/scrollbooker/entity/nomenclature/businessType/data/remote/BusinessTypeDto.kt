package com.example.scrollbooker.entity.nomenclature.businessType.data.remote

import com.google.gson.annotations.SerializedName

data class BusinessTypeDto(
    val id: Int,
    val name: String,
    val plural: String,

    @SerializedName("business_domain_id")
    val businessDomainId: Int,

    val url: String?,

    @SerializedName("thumbnail_url")
    val thumbnailUrl: String?
)