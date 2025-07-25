package com.example.scrollbooker.entity.booking.review.data.mappers

import com.example.scrollbooker.entity.booking.review.data.remote.ReviewCustomerDto
import com.example.scrollbooker.entity.booking.review.data.remote.ReviewDto
import com.example.scrollbooker.entity.booking.review.data.remote.ReviewProductDto
import com.example.scrollbooker.entity.booking.review.data.remote.ReviewServiceDto
import com.example.scrollbooker.entity.booking.review.domain.model.Review
import com.example.scrollbooker.entity.booking.review.domain.model.ReviewCustomer
import com.example.scrollbooker.entity.booking.review.domain.model.ReviewProduct
import com.example.scrollbooker.entity.booking.review.domain.model.ReviewService

fun ReviewDto.toDomain(): Review {
    return Review(
        id = id,
        rating = rating,
        review = review,
        customer = customer.toDomain(),
        service = service.toDomain(),
        product = product.toDomain(),
        likeCount = likeCount,
        isLiked = isLiked,
        isLikedByAuthor = isLikedByAuthor,
        createdAt = createdAt
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

fun ReviewServiceDto.toDomain(): ReviewService {
    return ReviewService(
        id = id,
        name = name
    )
}

fun ReviewProductDto.toDomain(): ReviewProduct {
    return ReviewProduct(
        id = id,
        name = name
    )
}