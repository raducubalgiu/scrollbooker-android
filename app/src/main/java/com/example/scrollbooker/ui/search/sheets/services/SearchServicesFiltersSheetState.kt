package com.example.scrollbooker.ui.search.sheets.services

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.core.extensions.toPrettyTime
import com.example.scrollbooker.ui.search.SearchRequestState
import kotlinx.parcelize.Parcelize
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import toDayMonthShort

@Parcelize
data class SearchServicesFiltersSheetState(
    val businessDomainId: Int? = null,
    val serviceDomainId: Int? = null,
    val serviceId: Int? = null,
    val selectedFilters: Map<Int, Int> = emptyMap(),
    val startDate: LocalDate? = null,
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null
) : Parcelable

fun SearchServicesFiltersSheetState.isClearAllEnabled(): Boolean {
    return listOf(
        serviceDomainId,
        serviceId,
        startDate,
        startTime,
        endTime
    ).any { it != null }
}

fun SearchServicesFiltersSheetState.isConfirmEnabled(request: SearchRequestState): Boolean {
    return listOf<Boolean>(
        businessDomainId != request.filters.businessDomainId,
        serviceId != request.filters.serviceId,
        selectedFilters != request.filters.selectedFilters,
        startDate != request.filters.startDate,
        startTime != request.filters.startTime,
        endTime != request.filters.endTime
    ).any { it == true }
}

@Composable
fun SearchServicesFiltersSheetState.dateTimeSummary(): String {
    val hasDate = startDate != null
    val hasTime = startTime != null || endTime != null

    if (!hasDate && !hasTime) return stringResource(R.string.anytimeAnyHour)

    val datePart = when {
        startDate != null -> startDate.toDayMonthShort()
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
        else -> stringResource(R.string.anytimeAnyHour)
    }
}

fun SearchServicesFiltersSheetState.isDateActive(): Boolean {
    return startDate != null || startTime != null || endTime != null
}