package com.example.scrollbooker.entity.search.data.mappers
import com.example.scrollbooker.entity.booking.business.data.mappers.toDomain
import com.example.scrollbooker.entity.search.data.remote.UserSearchDto
import com.example.scrollbooker.entity.search.domain.model.UserSearch

fun UserSearchDto.toDomain(): UserSearch {
    return UserSearch(
        recommendedBusiness = recommendedBusiness.map { it.toDomain() },
        recentlySearch = recentlySearch.map { it.toDomain() }
    )
}