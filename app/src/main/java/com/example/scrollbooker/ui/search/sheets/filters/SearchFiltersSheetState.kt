package com.example.scrollbooker.ui.search.sheets.filters
import android.os.Parcelable
import com.example.scrollbooker.core.enums.SearchSortEnum
import com.example.scrollbooker.ui.search.SearchFiltersState
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchFiltersSheetState(
    val maxPrice: Float? = 1500f,
    val sort: SearchSortEnum = SearchSortEnum.RECOMMENDED,
    val hasVideo: Boolean = false,
    val hasDiscount: Boolean = false,
    val isLastMinute: Boolean = false
) : Parcelable

fun SearchFiltersSheetState.clear(
    defaultMaxPrice: Float? = maxPrice
): SearchFiltersSheetState = copy(
    maxPrice = defaultMaxPrice,
    sort = SearchSortEnum.RECOMMENDED,
    hasVideo = false,
    hasDiscount = false,
    isLastMinute = false
)

fun SearchFiltersSheetState.applyOn(base: SearchFiltersState): SearchFiltersState =
    base.copy(
        maxPrice = maxPrice,
        sort = sort,
        hasVideo = hasVideo,
        hasDiscount = hasDiscount,
        isLastMinute = isLastMinute
    )

fun SearchFiltersSheetState.hasChangesComparedTo(base: SearchFiltersState): Boolean =
    this.applyOn(base) != base