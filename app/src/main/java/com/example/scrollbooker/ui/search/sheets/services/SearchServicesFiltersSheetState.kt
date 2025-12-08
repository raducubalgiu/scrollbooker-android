package com.example.scrollbooker.ui.search.sheets.services

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

@Parcelize
data class SearchServicesFiltersSheetState(
    val businessDomainId: Int? = null,
    val businessTypeId: Int? = null,
    val serviceId: Int? = null,
    val subFilterIds: Set<Int> = emptySet(),
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null
) : Parcelable

val SearchServicesFiltersSheetState.hasDateTime: Boolean
    get() = startDate != null || endDate != null || startTime != null || endTime != null