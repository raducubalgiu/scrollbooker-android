package com.example.scrollbooker.entity.nomenclature.filter.domain.model

import com.example.scrollbooker.core.enums.FilterTypeEnum

data class Filter(
    val id: Int,
    val name: String,
    val singleSelect: Boolean,
    val type: FilterTypeEnum?,
    val subFilters: List<SubFilter>
)

data class SubFilter(
    val id: Int,
    val name: String
)