package com.example.scrollbooker.shared.notification.data.mappers

import com.example.scrollbooker.shared.notification.data.remote.NotificationDto
import com.example.scrollbooker.shared.notification.data.remote.SenderDto
import com.example.scrollbooker.shared.notification.domain.model.Notification
import com.example.scrollbooker.shared.notification.domain.model.Sender

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