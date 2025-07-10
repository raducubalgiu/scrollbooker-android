package com.example.scrollbooker.entity.filter.data.remote

import com.google.gson.annotations.SerializedName

data class FilterDto(
    val id: Int,
    val name: String,

    @SerializedName("sub_filters")
    val subFilters: List<SubFilterDto>
)

data class SubFilterDto(
    val id: Int,
    val name: String
)