package com.example.scrollbooker.entity.nomenclature.service.data.mappers

import com.example.scrollbooker.entity.nomenclature.service.data.remote.ServiceFilterDto
import com.example.scrollbooker.entity.nomenclature.service.data.remote.ServiceFilterSubFilterDto
import com.example.scrollbooker.entity.nomenclature.service.data.remote.ServiceWithFiltersDto
import com.example.scrollbooker.entity.nomenclature.service.domain.model.ServiceFilter
import com.example.scrollbooker.entity.nomenclature.service.domain.model.ServiceFilterSubFilter
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

fun ServiceFilterDto.toDomain(): ServiceFilter {
    return ServiceFilter(
        id = id,
        name = name,
        subFilters = subFilters.map { it.toDomain() }
    )
}

fun ServiceFilterSubFilterDto.toDomain(): ServiceFilterSubFilter {
    return ServiceFilterSubFilter(
        id = id,
        name = name
    )
}