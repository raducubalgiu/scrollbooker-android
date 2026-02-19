package com.example.scrollbooker.entity.nomenclature.serviceDomain.data.remote

import com.google.gson.annotations.SerializedName

data class SelectedServiceDomainsWithServicesDto(
    val id: Int,
    val name: String,
    val services: List<SelectedServiceDto>
)

data class SelectedServiceDto(
    val id: Int,
    val name: String,

    @SerializedName("short_name")
    val shortName: String,

    @SerializedName("is_selected")
    val isSelected: Boolean
)