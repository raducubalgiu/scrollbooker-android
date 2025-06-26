package com.example.scrollbooker.entity.review.domain.model

data class Review(
    val id: Int,
    val rating: Int,
    val review: String,
    val customer: ReviewCustomer,
    val service: ReviewService,
    val product: ReviewProduct,
    val likeCount: Int,
    val isLiked: Boolean,
    val isLikedByAuthor: Boolean,
    val createdAt: String
)

data class ReviewCustomer(
    val id: Int,
    val username: String,
    val fullName: String,
    val avatar: String?,
)

data class ReviewService(
    val id: Int,
    val name: String
)

data class ReviewProduct(
    val id: Int,
    val name: String
)