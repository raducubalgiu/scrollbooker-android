package com.example.scrollbooker.shared.reviews.data.remote

import com.google.gson.annotations.SerializedName

data class ReviewDto(
    val id: Int,
    val rating: Int,
    val review: String,
    val customer: ReviewCustomerDto,
    val service: ReviewServiceDto,
    val product: ReviewProductDto,

    @SerializedName("like_count")
    val likeCount: Int,

    @SerializedName("is_liked")
    val isLiked: Boolean,

    @SerializedName("is_liked_by_author")
    val isLikedByAuthor: Boolean,

    @SerializedName("created_at")
    val createdAt: String
)

data class ReviewCustomerDto(
    val id: Int,
    val username: String,

    @SerializedName("fullname")
    val fullName: String,

    val avatar: String?,
)

data class ReviewServiceDto(
    val id: Int,
    val name: String
)

data class ReviewProductDto(
    val id: Int,
    val name: String
)