package com.example.scrollbooker.entity.booking.review.data.remote

import com.google.gson.annotations.SerializedName

data class ReviewMiniDto(
    val id: Int,
    val review: String?,
    val rating: Int,

    @SerializedName("customer_id")
    val customerId: Int,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("service_id")
    val serviceId: Int,

    @SerializedName("product_id")
    val productId: Int,

    @SerializedName("parent_id")
    val parentId: Int?
)