package com.example.scrollbooker.shared.reviews.data.mappers

import com.example.scrollbooker.shared.reviews.data.remote.ReviewCustomerDto
import com.example.scrollbooker.shared.reviews.data.remote.ReviewDto
import com.example.scrollbooker.shared.reviews.data.remote.ReviewProductDto
import com.example.scrollbooker.shared.reviews.data.remote.ReviewServiceDto
import com.example.scrollbooker.shared.reviews.domain.model.Review
import com.example.scrollbooker.shared.reviews.domain.model.ReviewCustomer
import com.example.scrollbooker.shared.reviews.domain.model.ReviewProduct
import com.example.scrollbooker.shared.reviews.domain.model.ReviewService

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