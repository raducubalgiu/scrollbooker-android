package com.example.scrollbooker.entity.booking.review.data.remote

import com.google.gson.annotations.SerializedName

data class ReviewCreateRequest(
    val review: String?,
    val rating: Int,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("product_id")
    val productId: Int,

    @SerializedName("parent_id")
    val parentId: Int?
)