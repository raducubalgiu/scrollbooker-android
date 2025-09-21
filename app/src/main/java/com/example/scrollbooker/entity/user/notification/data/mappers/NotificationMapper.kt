package com.example.scrollbooker.entity.user.notification.data.mappers

import com.example.scrollbooker.core.enums.NotificationTypeEnum
import com.example.scrollbooker.entity.user.notification.data.remote.NotificationDto
import com.example.scrollbooker.entity.user.notification.data.remote.SenderDto
import com.example.scrollbooker.entity.user.notification.domain.model.Notification
import com.example.scrollbooker.entity.user.notification.domain.model.Sender

fun NotificationDto.toDomain(): Notification {
    return Notification(
        id = id,
        type = NotificationTypeEnum.fromKey(type),
        senderId = senderId,
        receiverId = receiverId,
        data = data,
        message = message,
        isRead = isRead,
        isDeleted = isDeleted,
        sender = sender.toDomain(),
        isFollow = isFollow
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