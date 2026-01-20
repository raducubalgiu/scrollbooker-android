package com.example.scrollbooker.ui.myBusiness.myProducts.components

import java.math.BigDecimal

data class FiltersActions(
    val singleOption: (filterId: Int, subFilterId: Int?) -> Unit,
    val toggleMultiOption: (filterId: Int, subFilterId: Int) -> Unit,
    val setRange: (filterId: Int, min: BigDecimal?, max: BigDecimal?) -> Unit,
    val toggleApplicable: (filterId: Int) -> Unit
)