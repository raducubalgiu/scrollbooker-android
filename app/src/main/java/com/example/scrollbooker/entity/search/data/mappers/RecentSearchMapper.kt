package com.example.scrollbooker.entity.search.data.mappers

import com.example.scrollbooker.entity.search.data.remote.RecentSearchDto
import com.example.scrollbooker.entity.search.data.remote.RecentSearchFilterDto
import com.example.scrollbooker.entity.search.data.remote.RecentSearchServiceDomainDto
import com.example.scrollbooker.entity.search.data.remote.RecentSearchServiceDto
import com.example.scrollbooker.entity.search.data.remote.RecentSearchSubFilerDto
import com.example.scrollbooker.entity.search.domain.model.RecentSearch
import com.example.scrollbooker.entity.search.domain.model.RecentSearchFilter
import com.example.scrollbooker.entity.search.domain.model.RecentSearchService
import com.example.scrollbooker.entity.search.domain.model.RecentSearchServiceDomain
import com.example.scrollbooker.entity.search.domain.model.RecentSearchSubFiler

fun RecentSearchDto.toDomain(): RecentSearch {
    return RecentSearch(
        id = this.id,
        businessDomainId = this.businessDomainId,
        serviceDomain = this.serviceDomain.toDomain(),
        services = this.services.map { it.toDomain() }
    )
}

fun RecentSearchServiceDomainDto.toDomain(): RecentSearchServiceDomain {
    return RecentSearchServiceDomain(
        id = this.id,
        name = this.name
    )
}

fun RecentSearchServiceDto.toDomain(): RecentSearchService {
    return RecentSearchService(
        id = this.id,
        name = this.name,
        filters = this.filters.map { it.toDomain() }
    )
}

fun RecentSearchFilterDto.toDomain(): RecentSearchFilter {
    return RecentSearchFilter(
        id = this.id,
        name = this.name,
        subFilters = this.subFilters.map { it.toDomain() }
    )
}

fun RecentSearchSubFilerDto.toDomain(): RecentSearchSubFiler {
    return RecentSearchSubFiler(
        id = this.id,
        name = this.name
    )
}