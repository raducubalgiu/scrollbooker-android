package com.example.scrollbooker.entity.user.notification.domain.model
import com.example.scrollbooker.core.enums.NotificationTypeEnum
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial

interface NotificationData

data class Notification(
    val id: Int,
    val type: NotificationTypeEnum,
    val senderId: Int,
    val receiverId: Int,
    val data: NotificationData?,
    val message: String?,
    val isRead: Boolean,
    val isDeleted: Boolean,
    val sender: UserSocial,
    val isFollow: Boolean
)

object FollowNotificationData : NotificationData

data class LikePostNotificationData(
    val postId: Int,
    val actorIds: List<Int>,
    val postUrl: String?,
    val totalCount: Int
) : NotificationData

data class CommentPostNotificationData(
    val postId: Int,
    val actorIds: List<Int>,
    val postUrl: String?,
    val totalCount: Int
) : NotificationData

data class RepostNotificationData(
    val postId: Int,
    val actorIds: List<Int>,
    val postUrl: String?,
    val totalCount: Int
) : NotificationData

data class MentionPostNotificationData(
    val postId: Int,
    val commentId: Int?
) : NotificationData

data class AppointmentBookedNotificationData(
    val appointmentId: Int,
    val customerId: Int,
    val startDate: String,
    val customerFullName: String
) : NotificationData

data class AppointmentCanceledNotificationData(
    val appointmentId: Int,
    val canceledByUserId: Int,
    val canceledReason: String
) : NotificationData

data class AppointmentRescheduledNotificationData(
    val appointmentId: Int,
    val oldStartDate: String,
    val newStartDate: Int
) : NotificationData

data class AppointmentReminderNotificationData(
    val appointmentId: Int,
    val startDate: Int
) : NotificationData

data class AppointmentReviewedNotificationData(
    val appointmentId: Int,
    val reviewId: Int,
    val rating: Int
) : NotificationData

data class EmploymentRequestNotificationData(
    val employmentRequestId: Int,
    val professionId: Int,
    val professionName: String,
    val businessId: Int
) : NotificationData

data class EmploymentRequestAcceptedNotificationData(
    val employmentRequestId: Int,
    val businessId: Int
) : NotificationData

data class EmploymentRequestDeniedNotificationData(
    val employmentRequestId: Int,
    val businessId: Int
) : NotificationData

data class BusinessValidationNotificationData(
    val businessId: Int,
    val isApproved: Boolean,
    val reason: String?
) : NotificationData
