package com.example.scrollbooker.entity.booking.review.data.remote

import com.example.scrollbooker.entity.booking.review.domain.model.ReviewMini

fun ReviewMiniDto.toDomain(): ReviewMini {
    return ReviewMini(
        id = id,
        review = review,
        rating = rating,
        customerId = customerId,
        userId = userId,
        serviceId = serviceId,
        productId = productId,
        parentId = parentId
    )
}