package com.example.scrollbooker.entity.businessDomain.data.remote

import com.google.gson.annotations.SerializedName

data class BusinessDomainDto(
    val id: Int,
    val name: String,

    @SerializedName("short_name")
    val shortName: String
)