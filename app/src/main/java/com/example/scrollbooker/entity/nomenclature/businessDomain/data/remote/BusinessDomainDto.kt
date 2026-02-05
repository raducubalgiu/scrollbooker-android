package com.example.scrollbooker.entity.nomenclature.businessDomain.data.remote

import com.example.scrollbooker.entity.nomenclature.serviceDomain.data.remote.ServiceDomainDto
import com.google.gson.annotations.SerializedName

data class BusinessDomainDto(
    val id: Int,
    val name: String,

    @SerializedName("short_name")
    val shortName: String,

    @SerializedName("service_domains")
    val serviceDomains: List<ServiceDomainDto> = emptyList()
)