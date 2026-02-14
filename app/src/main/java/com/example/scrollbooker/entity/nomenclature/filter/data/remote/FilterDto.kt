package com.example.scrollbooker.entity.nomenclature.filter.data.remote

import com.google.gson.annotations.SerializedName

data class FilterDto(
    val id: Int,
    val name: String,

    @SerializedName("single_select")
    val singleSelect: Boolean,

    val type: String,

    @SerializedName("sub_filters")
    val subFilters: List<SubFilterDto>,

    val unit: String?
)

data class SubFilterDto(
    val id: Int,
    val name: String,
    val description: String?
)