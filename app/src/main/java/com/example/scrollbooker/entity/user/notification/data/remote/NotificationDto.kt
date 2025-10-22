package com.example.scrollbooker.entity.user.notification.data.remote

import com.example.scrollbooker.entity.user.userSocial.data.remote.UserSocialDto
import com.google.gson.annotations.SerializedName

data class NotificationDto(
    val id: Int,
    val type: String,

    @SerializedName("sender_id")
    val senderId: Int,

    @SerializedName("receiver_id")
    val receiverId: Int,

    val data: EmploymentRequestDataDto?,
    val message: String?,

    @SerializedName("is_read")
    val isRead: Boolean,

    @SerializedName("is_deleted")
    val isDeleted: Boolean,

    val sender: UserSocialDto,

    @SerializedName("is_follow")
    val isFollow: Boolean
)

data class EmploymentRequestDataDto(
    @SerializedName("employment_request_id")
    val employmentRequestId: Int,

    @SerializedName("profession_id")
    val professionId: Int,
)