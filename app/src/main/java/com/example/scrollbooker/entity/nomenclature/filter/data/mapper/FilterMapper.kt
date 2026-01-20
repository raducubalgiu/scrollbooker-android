package com.example.scrollbooker.entity.nomenclature.filter.data.mapper

import com.example.scrollbooker.core.enums.FilterTypeEnum
import com.example.scrollbooker.entity.nomenclature.filter.data.remote.FilterDto
import com.example.scrollbooker.entity.nomenclature.filter.data.remote.SubFilterDto
import com.example.scrollbooker.entity.nomenclature.filter.domain.model.Filter
import com.example.scrollbooker.entity.nomenclature.filter.domain.model.SubFilter

fun FilterDto.toDomain(): Filter {
    return Filter(
        id = id,
        name = name,
        singleSelect = singleSelect,
        type = FilterTypeEnum.fromKey(type),
        subFilters = subFilters.map { it.toDomain() },
        unit = unit
    )
}

fun SubFilterDto.toDomain(): SubFilter {
    return SubFilter(
        id = id,
        name = name
    )
}