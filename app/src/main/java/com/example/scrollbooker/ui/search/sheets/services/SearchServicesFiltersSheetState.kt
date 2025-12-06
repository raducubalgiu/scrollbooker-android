package com.example.scrollbooker.ui.search.sheets.services

data class SearchServicesFiltersSheetState(
    val businessDomainId: Int? = null,
    val businessTypeId: Int? = null,
    val serviceId: Int? = null,
    val subFilterIds: Set<Int> = emptySet()
)