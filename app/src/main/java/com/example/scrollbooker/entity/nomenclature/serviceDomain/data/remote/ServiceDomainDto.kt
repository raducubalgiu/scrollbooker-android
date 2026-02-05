package com.example.scrollbooker.entity.nomenclature.serviceDomain.data.remote

import com.google.gson.annotations.SerializedName

data class ServiceDomainDto(
    val id: Int,
    val name: String,
    val description: String?,
    val url: String?,

    @SerializedName("thumbnail_url")
    val thumbnailUrl: String?
)