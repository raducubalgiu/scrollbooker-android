package com.example.scrollbooker.entity.nomenclature.service.domain.model

data class ServiceWithFilters(
    val id: Int,
    val name: String,
    val shortName: String,
    val description: String?,
    val businessDomainId: Int,
    val filters: List<ServiceFilter>
)

data class ServiceFilter(
    val id: Int,
    val name: String,
    val subFilters: List<ServiceFilterSubFilter>
)

data class ServiceFilterSubFilter(
    val id: Int,
    val name: String
)