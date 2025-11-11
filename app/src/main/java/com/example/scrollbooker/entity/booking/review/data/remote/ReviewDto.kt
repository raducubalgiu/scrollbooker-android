package com.example.scrollbooker.entity.booking.review.data.remote

import com.google.gson.annotations.SerializedName

data class ReviewDto(
    val id: Int,
    val rating: Int,
    val review: String,

    @SerializedName("product_business_owner")
    val productBusinessOwner: ReviewProductBusinessOwnerDto,

    val customer: ReviewCustomerDto,
    val service: ReviewServiceDto,
    val product: ReviewProductDto,

    @SerializedName("like_count")
    val likeCount: Int,

    @SerializedName("is_liked")
    val isLiked: Boolean,

    @SerializedName("is_liked_by_product_owner")
    val isLikedByProductOwner: Boolean,

    @SerializedName("created_at")
    val createdAt: String
)

data class ReviewProductBusinessOwnerDto(
    val id: Int,
    val username: String,

    @SerializedName("fullname")
    val fullName: String,

    val avatar: String?,
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