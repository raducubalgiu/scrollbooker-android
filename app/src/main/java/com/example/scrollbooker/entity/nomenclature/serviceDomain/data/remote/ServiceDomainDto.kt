package com.example.scrollbooker.entity.nomenclature.serviceDomain.data.remote

import com.google.gson.annotations.SerializedName

data class ServiceDomainDto(
    val id: Int,
    val name: String,

    @SerializedName("business_domain_id")
    val businessDomainId: Int
)