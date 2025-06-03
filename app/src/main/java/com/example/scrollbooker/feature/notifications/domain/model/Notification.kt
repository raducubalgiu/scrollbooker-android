package com.example.scrollbooker.feature.notifications.domain.model

data class Notification(
    val id: Int,
    val type: String,
    val senderId: Int,
    val receiverId: Int,
    val data: Map<String, Any>?,
    val message: String?,
    val isRead: Boolean,
    val isDeleted: Boolean,
    val sender: Sender
)

data class Sender(
    val id: Int,
    val fullName: String,
    val username: String,
    val avatar: String
)