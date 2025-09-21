package com.example.scrollbooker.entity.user.notification.domain.model
import com.example.scrollbooker.core.enums.NotificationTypeEnum

data class Notification(
    val id: Int,
    val type: NotificationTypeEnum,
    val senderId: Int,
    val receiverId: Int,
    val data: Map<String, Any>?,
    val message: String?,
    val isRead: Boolean,
    val isDeleted: Boolean,
    val sender: Sender,
    val isFollow: Boolean
)

data class Sender(
    val id: Int,
    val fullName: String?,
    val username: String,
    val avatar: String?
)