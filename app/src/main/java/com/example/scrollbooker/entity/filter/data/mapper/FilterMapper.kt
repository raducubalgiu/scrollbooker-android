package com.example.scrollbooker.entity.filter.data.mapper

import com.example.scrollbooker.entity.filter.data.remote.FilterDto
import com.example.scrollbooker.entity.filter.data.remote.SubFilterDto
import com.example.scrollbooker.entity.filter.domain.model.Filter
import com.example.scrollbooker.entity.filter.domain.model.SubFilter

fun FilterDto.toDomain(): Filter {
    return Filter(
        id = id,
        name = name,
        subFilters = subFilters.map { it.toDomain() }
    )
}

fun SubFilterDto.toDomain(): SubFilter {
    return SubFilter(
        id = id,
        name = name
    )
}