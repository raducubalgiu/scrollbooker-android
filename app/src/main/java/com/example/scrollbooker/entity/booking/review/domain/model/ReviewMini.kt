package com.example.scrollbooker.entity.booking.review.domain.model

data class ReviewMini(
    val id: Int,
    val review: String?,
    val rating: Int,
    val appointmentId: Int,
    val customerId: Int,
    val userId: Int,
    val serviceId: Int,
    val productId: Int,
    val parentId: Int?
)