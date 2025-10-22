package com.example.scrollbooker.entity.user.notification.domain.model
import com.example.scrollbooker.core.enums.NotificationTypeEnum
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial

data class Notification(
    val id: Int,
    val type: NotificationTypeEnum,
    val senderId: Int,
    val receiverId: Int,
    val data: EmploymentRequestData?,
    val message: String?,
    val isRead: Boolean,
    val isDeleted: Boolean,
    val sender: UserSocial,
    val isFollow: Boolean
)

data class EmploymentRequestData(
    val employmentRequestId: Int,
    val professionId: Int,
)