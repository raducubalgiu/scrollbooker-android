package com.example.scrollbooker.entity.nomenclature.filter.data.remote

import com.google.gson.annotations.SerializedName

data class FilterDto(
    val id: Int,
    val name: String,

    @SerializedName("single_select")
    val singleSelect: Boolean,

    @SerializedName("sub_filters")
    val subFilters: List<SubFilterDto>
)

data class SubFilterDto(
    val id: Int,
    val name: String
)