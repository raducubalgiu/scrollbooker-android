package com.example.scrollbooker.ui.search.sheets.services

import android.os.Parcelable
import com.example.scrollbooker.core.extensions.toPrettyTime
import com.example.scrollbooker.ui.search.SearchFiltersState
import kotlinx.parcelize.Parcelize
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import toDayMonthShort

@Parcelize
data class SearchServicesFiltersSheetState(
    val businessDomainId: Int? = null,
    val serviceDomainId: Int? = null,
    val serviceId: Int? = null,
    val subFilterIds: Set<Int> = emptySet(),
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null
) : Parcelable

fun SearchServicesFiltersSheetState.hasActiveFilters(): Boolean {
    return serviceDomainId != null ||
            serviceId != null ||
            subFilterIds.isNotEmpty() ||
            startDate != null ||
            endDate != null ||
            startTime != null ||
            endTime != null
}

fun SearchServicesFiltersSheetState.hasDateAndTimeFilters(): Boolean {
    return startDate != null ||
            endDate != null ||
            startTime != null ||
            endTime != null
}

fun SearchServicesFiltersSheetState.applyOn(
    base: SearchFiltersState
): SearchFiltersState =
    base.copy(
        serviceDomainId = serviceDomainId,
        serviceId = serviceId,
        subFilterIds = subFilterIds,
        startDate = startDate,
        endDate = endDate,
        startTime = startTime,
        endTime = endTime
    )

fun SearchServicesFiltersSheetState.hasChangesComparedTo(
    base: SearchFiltersState
): Boolean = this.applyOn(base) != base


fun SearchServicesFiltersSheetState.dateTimeSummary(): String? {
    val hasDate = startDate != null || endDate != null
    val hasTime = startTime != null || endTime != null

    if (!hasDate && !hasTime) return null

    val datePart = when {
        startDate != null && endDate != null && startDate == endDate -> startDate.toDayMonthShort()
        startDate != null && endDate != null -> "${startDate.toDayMonthShort()} – ${endDate.toDayMonthShort()}"
        else -> null
    }

    val timePart = when {
        startTime != null && endTime != null -> "${startTime.toPrettyTime()} – ${endTime.toPrettyTime()}"
        else -> null
    }

    return when {
        datePart != null && timePart != null -> "$datePart • $timePart"
        datePart != null -> datePart
        timePart != null -> timePart
        else -> null
    }
}