package com.example.scrollbooker.entity.search.data.remote

import com.google.gson.annotations.SerializedName

data class RecentSearchDto(
    val id: Int,

    @SerializedName("business_domain_id")
    val businessDomainId: Int,

    @SerializedName("service_domain")
    val serviceDomain: RecentSearchServiceDomainDto,

    val services: List<RecentSearchServiceDto>
)

data class RecentSearchServiceDomainDto(
    val id: Int,
    val name: String,
)

data class RecentSearchServiceDto(
    val id: Int,
    val name: String,
    val filters: List<RecentSearchFilterDto>
)

data class RecentSearchFilterDto(
    val id: Int,
    val name: String,

    @SerializedName("sub_filters")
    val subFilters: List<RecentSearchSubFilerDto>
)

data class RecentSearchSubFilerDto(
    val id: Int,
    val name: String
)