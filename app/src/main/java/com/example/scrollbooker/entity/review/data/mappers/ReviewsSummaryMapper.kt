package com.example.scrollbooker.entity.review.data.mappers

import com.example.scrollbooker.entity.review.data.remote.RatingBreakdownDto
import com.example.scrollbooker.entity.review.data.remote.ReviewsSummaryDto
import com.example.scrollbooker.entity.review.domain.model.RatingBreakdown
import com.example.scrollbooker.entity.review.domain.model.ReviewsSummary

fun ReviewsSummaryDto.toDomain(): ReviewsSummary {
    return ReviewsSummary(
        averageRating = averageRating,
        totalReviews = totalReviews,
        breakdown = breakdown.map { it.toDomain() }
    )
}

fun RatingBreakdownDto.toDomain(): RatingBreakdown {
    return RatingBreakdown(
        rating = rating,
        count = count
    )
}