package com.example.scrollbooker.entity.nomenclature.service.domain.model

import com.example.scrollbooker.entity.nomenclature.filter.domain.model.Filter

data class ServiceWithFilters(
    val id: Int,
    val name: String,
    val shortName: String,
    val description: String?,
    val businessDomainId: Int,
    val filters: List<Filter>
)