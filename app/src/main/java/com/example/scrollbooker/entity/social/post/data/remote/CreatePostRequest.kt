package com.example.scrollbooker.entity.social.post.data.remote

import com.google.gson.annotations.SerializedName

data class CreatePostRequest(
    val description: String? = null,
    val provider: String,

    @SerializedName("provider_uid")
    val providerUid: String,

    @SerializedName("order_index")
    val orderIndex: Int = 0,

    @SerializedName("business_or_employee_id")
    val businessOrEmployeeId: Int?,

    @SerializedName("is_video_review")
    val isVideoReview: Boolean,

    @SerializedName("video_review_message")
    val videoReviewMessage: String?,

    val rating: Int?
)