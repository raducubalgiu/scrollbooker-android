package com.example.scrollbooker.feature.reviews.data.mappers
import com.example.scrollbooker.feature.reviews.data.remote.RatingBreakdownDto
import com.example.scrollbooker.feature.reviews.data.remote.ReviewsSummaryDto
import com.example.scrollbooker.feature.reviews.domain.model.RatingBreakdown
import com.example.scrollbooker.feature.reviews.domain.model.ReviewsSummary

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