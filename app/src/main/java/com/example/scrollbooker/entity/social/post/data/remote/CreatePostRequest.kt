package com.example.scrollbooker.entity.social.post.data.remote

import com.google.gson.annotations.SerializedName

data class CreatePostRequest(
    val description: String? = null,
    val provider: String,

    @SerializedName("provider_uid")
    val providerUid: String,

    @SerializedName("order_index")
    val orderIndex: Int = 0,

    @SerializedName("linked_product_ids")
    val linkedProductIds: List<Int>,

    @SerializedName("video_review_message")
    val videoReviewMessage: String?,

    @SerializedName("is_video_review")
    val isVideoReview: Boolean,

    val rating: Int?,

    @SerializedName("business_or_employee_id")
    val businessOrEmployeeId: Int?,
)