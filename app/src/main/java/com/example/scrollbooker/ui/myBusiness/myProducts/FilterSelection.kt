package com.example.scrollbooker.ui.myBusiness.myProducts

import java.math.BigDecimal

sealed interface FilterSelection {
    data class Options(val ids: Set<Int> = emptySet()): FilterSelection
    data class Range(
        val minim: BigDecimal?,
        val maxim: BigDecimal?,
        val isNotApplicable: Boolean = false
    ): FilterSelection
}

typealias SelectedFilters = Map<Int, FilterSelection>