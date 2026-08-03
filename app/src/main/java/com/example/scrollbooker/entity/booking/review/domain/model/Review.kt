package com.example.scrollbooker.entity.booking.review.domain.model

data class Review(
    val id: Int,
    val rating: Int,
    val review: String,
    val productBusinessOwner: ReviewProductBusinessOwner,
    val customer: ReviewCustomer,
    val likeCount: Int,
    val isLiked: Boolean,
    val isLikedByProductOwner: Boolean,
    val createdAt: String
)

data class ReviewProductBusinessOwner(
    val id: Int,
    val username: String,
    val fullName: String,
    val avatar: String?,
)

data class ReviewCustomer(
    val id: Int,
    val username: String,
    val fullName: String,
    val avatar: String?,
)