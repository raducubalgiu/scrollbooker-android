package com.example.scrollbooker.entity.search.data.mappers
import com.example.scrollbooker.entity.search.data.remote.RecentlySearchDto
import com.example.scrollbooker.entity.search.domain.model.RecentlySearch

fun RecentlySearchDto.toDomain(): RecentlySearch {
    return RecentlySearch(
        id = id,
        keyword = keyword
    )
}