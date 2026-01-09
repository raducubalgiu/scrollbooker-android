package com.example.scrollbooker.entity.nomenclature.filter.domain.model

data class Filter(
    val id: Int,
    val name: String,
    val singleSelect: Boolean,
    val subFilters: List<SubFilter>
)

data class SubFilter(
    val id: Int,
    val name: String
)