package com.example.scrollbooker.entity.booking.review.domain.model

data class ReviewsSummary(
    val averageRating: Float,
    val totalReviews: Int,
    val breakdown: List<RatingBreakdown>
)

data class RatingBreakdown(
    val rating: Int,
    val count: Int
)