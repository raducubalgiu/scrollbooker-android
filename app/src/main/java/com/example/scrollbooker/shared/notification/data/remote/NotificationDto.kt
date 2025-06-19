package com.example.scrollbooker.shared.notification.data.remote

import com.google.gson.annotations.SerializedName

data class NotificationDto(
    val id: Int,
    val type: String,

    @SerializedName("sender_id")
    val senderId: Int,

    @SerializedName("receiver_id")
    val receiverId: Int,

    val data: Map<String, Any>?,
    val message: String?,

    @SerializedName("is_read")
    val isRead: Boolean,

    @SerializedName("is_deleted")
    val isDeleted: Boolean,

    val sender: SenderDto
)

data class SenderDto(
    val id: Int,

    @SerializedName("fullname")
    val fullName: String?,

    val username: String,
    val avatar: String?
)