package com.example.scrollbooker.entity.nomenclature.service.data.remote

import com.google.gson.annotations.SerializedName

data class ServiceWithFiltersDto(
    val id: Int,
    val name: String,

    @SerializedName("short_name")
    val shortName: String,
    val description: String?,

    @SerializedName("business_domain_id")
    val businessDomainId: Int,
    val filters: List<ServiceFilterDto>
)

data class ServiceFilterDto(
    val id: Int,
    val name: String,

    @SerializedName("sub_filters")
    val subFilters: List<ServiceFilterSubFilterDto>
)

data class ServiceFilterSubFilterDto(
    val id: Int,
    val name: String
)