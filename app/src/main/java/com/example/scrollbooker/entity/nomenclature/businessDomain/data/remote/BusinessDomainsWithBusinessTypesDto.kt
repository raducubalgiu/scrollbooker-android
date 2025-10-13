package com.example.scrollbooker.entity.nomenclature.businessDomain.data.remote

import com.example.scrollbooker.entity.nomenclature.businessType.domain.model.BusinessType
import com.google.gson.annotations.SerializedName

data class BusinessDomainsWithBusinessTypesDto(
    val id: Int,
    val name: String,

    @SerializedName("short_name")
    val shortName: String,

    @SerializedName("business_types")
    val businessTypes: List<BusinessType>
)