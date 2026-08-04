package com.example.scrollbooker.entity.social.post.data.remote

import com.google.gson.annotations.SerializedName

data class CreateVideoReviewRequest(
    @SerializedName("business_or_employee_id")
    val businessOrEmployeeId: Int,

    @SerializedName("appointment_id")
    val appointmentId: Int,

    val review: String?,
    val rating: Int,
    val description: String?,
    val provider: String,

    @SerializedName("provider_uid")
    val providerUid: String,

    @SerializedName("order_index")
    val orderIndex: Int = 0
)