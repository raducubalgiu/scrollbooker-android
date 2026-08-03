package com.example.scrollbooker.entity.booking.review.data.mappers

import com.example.scrollbooker.entity.booking.review.data.remote.ReviewCustomerDto
import com.example.scrollbooker.entity.booking.review.data.remote.ReviewDto
import com.example.scrollbooker.entity.booking.review.data.remote.ReviewProductBusinessOwnerDto
import com.example.scrollbooker.entity.booking.review.domain.model.Review
import com.example.scrollbooker.entity.booking.review.domain.model.ReviewCustomer
import com.example.scrollbooker.entity.booking.review.domain.model.ReviewProductBusinessOwner

fun ReviewDto.toDomain(): Review {
    return Review(
        id = id,
        rating = rating,
        review = review,
        productBusinessOwner = productBusinessOwner.toDomain(),
        customer = customer.toDomain(),
        likeCount = likeCount,
        isLiked = isLiked,
        isLikedByProductOwner = isLikedByProductOwner,
        createdAt = createdAt
    )
}

fun ReviewProductBusinessOwnerDto.toDomain(): ReviewProductBusinessOwner {
    return ReviewProductBusinessOwner(
        id = id,
        username = username,
        fullName = fullName,
        avatar = avatar
    )
}

fun ReviewCustomerDto.toDomain(): ReviewCustomer {
    return ReviewCustomer(
        id = id,
        username = username,
        fullName = fullName,
        avatar = avatar
    )
}