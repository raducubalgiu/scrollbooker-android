package com.example.scrollbooker.feature.notifications.data.mappers

import com.example.scrollbooker.feature.notifications.data.remote.NotificationDto
import com.example.scrollbooker.feature.notifications.data.remote.SenderDto
import com.example.scrollbooker.feature.notifications.domain.model.Notification
import com.example.scrollbooker.feature.notifications.domain.model.Sender

fun NotificationDto.toDomain(): Notification {
    return Notification(
        id = id,
        type = type,
        senderId = senderId,
        receiverId = receiverId,
        data = data,
        message = message,
        isRead = isRead,
        isDeleted = isDeleted,
        sender = sender.toDomain()
    )
}

fun SenderDto.toDomain(): Sender {
    return Sender(
        id = id,
        fullName = fullName,
        username = username,
        avatar = avatar
    )
}