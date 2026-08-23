package com.example.scrollbooker.entity.search.domain.model

data class RecentSearch(
    val id: Int,
    val businessDomainId: Int,
    val serviceDomain: RecentSearchServiceDomain,
    val services: List<RecentSearchService>
)

data class RecentSearchServiceDomain(
    val id: Int,
    val name: String,
)

data class RecentSearchService(
    val id: Int,
    val name: String,
    val filters: List<RecentSearchFilter>
)

data class RecentSearchFilter(
    val id: Int,
    val name: String,
    val subFilters: List<RecentSearchSubFiler>
)

data class RecentSearchSubFiler(
    val id: Int,
    val name: String
)

fun RecentSearch.displayLabel(): String? {
    val service = services.firstOrNull() ?: return serviceDomain.name

    val subFilterNames = service.filters
        .flatMap { it.subFilters }
        .map { it.name }

    return if (subFilterNames.isNotEmpty()) {
        "${service.name} • ${subFilterNames.joinToString(" & ")}"
    } else {
        null
    }
}