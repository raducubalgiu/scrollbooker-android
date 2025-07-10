package com.example.scrollbooker.entity.filter.domain.model

data class Filter(
    val id: Int,
    val name: String,
    val subFilters: List<SubFilter>
)

data class SubFilter(
    val id: Int,
    val name: String
)