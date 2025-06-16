package com.example.scrollbooker.feature.reviews.data.remote

import com.google.gson.annotations.SerializedName

data class ReviewsSummaryDto(
    @SerializedName("average_rating")
    val averageRating: Float,

    @SerializedName("total_reviews")
    val totalReviews: Int,

    val breakdown: List<RatingBreakdownDto>
)

data class RatingBreakdownDto(
    val rating: Int,
    val count: Int
)