package com.example.scrollbooker.core.enums

import androidx.annotation.StringRes
import com.example.scrollbooker.R

enum class SearchSortEnum(
    val raw: String,
    @StringRes val labelRes: Int
) {
    RECOMMENDED("recommended", R.string.recommended),
    DISTANCE("distance", R.string.distance),
    RATING("rating", R.string.rating),
    PRICE("price", R.string.price)
}