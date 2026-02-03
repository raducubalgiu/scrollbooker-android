package com.example.scrollbooker.entity.booking.review.data.remote

import com.google.gson.annotations.SerializedName

data class ReviewsSummaryDto(
    @SerializedName("ratings_average")
    val ratingsAverage: Float,

    @SerializedName("ratings_count")
    val ratingsCount: Int,

    val breakdown: List<RatingBreakdownDto>
)

data class RatingBreakdownDto(
    val rating: Int,
    val count: Int
)