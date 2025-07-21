package com.example.scrollbooker.entity.booking.review.data.remote

import androidx.annotation.StringRes
import com.example.scrollbooker.R

enum class ReviewLabel(@StringRes val labelRes: Int) {
    ONE(R.string.rating_1),
    TWO(R.string.rating_2),
    THREE(R.string.rating_3),
    FOUR(R.string.rating_4),
    FIVE(R.string.rating_5);

    companion object {
        fun fromValue(value: Int): ReviewLabel {
            return ReviewLabel.entries.find { it.ordinal == value - 1 } ?: THREE
        }
    }
}