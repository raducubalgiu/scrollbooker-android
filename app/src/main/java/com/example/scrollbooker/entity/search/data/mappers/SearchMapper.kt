package com.example.scrollbooker.entity.search.data.mappers
import com.example.scrollbooker.entity.search.data.remote.SearchDto
import com.example.scrollbooker.entity.search.data.remote.SearchServiceBusinessTypeDto
import com.example.scrollbooker.entity.search.domain.model.Search
import com.example.scrollbooker.entity.search.domain.model.SearchServiceBusinessType

fun SearchDto.toDomain(): Search {
    return Search(
        type = type,
        label = label,
        user = user,
        service = service?.toDomain(),
        businessType = businessType?.toDomain()
    )
}

fun SearchServiceBusinessTypeDto.toDomain(): SearchServiceBusinessType {
    return SearchServiceBusinessType(
        id = id,
        name = name
    )
}