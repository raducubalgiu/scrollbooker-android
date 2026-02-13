package com.example.scrollbooker.entity.nomenclature.service.data.mappers

import com.example.scrollbooker.entity.nomenclature.filter.data.mapper.toDomain
import com.example.scrollbooker.entity.nomenclature.service.data.remote.ServiceWithFiltersDto
import com.example.scrollbooker.entity.nomenclature.service.domain.model.ServiceWithFilters

fun ServiceWithFiltersDto.toDomain(): ServiceWithFilters {
    return ServiceWithFilters(
        id = id,
        name = name,
        shortName = shortName,
        description = description,
        businessDomainId = businessDomainId,
        filters = filters.map { it.toDomain() }
    )
}