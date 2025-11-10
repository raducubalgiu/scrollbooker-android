package com.example.scrollbooker.entity.booking.review.data.mappers

import com.example.scrollbooker.entity.booking.review.data.remote.ReviewMiniDto
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
        parentId = parentId,
        appointmentId = appointmentId
    )
}