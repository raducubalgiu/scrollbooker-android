package com.example.scrollbooker.entity.user.notification.domain.model
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
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

@Composable
fun Notification.resolveDescription(): String {
    return when (val data = this.data) {
        is FollowNotificationData -> stringResource(R.string.notification_started_following_you)
        is LikePostNotificationData -> {
            if (data.totalCount > 1) stringResource(R.string.notification_like_multiple, data.totalCount - 1)
            else stringResource(R.string.notification_like_single)
        }
        is CommentPostNotificationData -> stringResource(R.string.notification_comment_post)
        is RepostNotificationData -> stringResource(R.string.notification_repost)
        is MentionPostNotificationData -> stringResource(R.string.notification_mention_post)
        is AppointmentBookedNotificationData -> stringResource(R.string.notification_appointment_booked, data.startDate)
        is AppointmentCanceledNotificationData -> {
            if (data.canceledReason.isNotBlank()) stringResource(R.string.notification_appointment_canceled_with_reason, data.canceledReason)
            else stringResource(R.string.notification_appointment_canceled)
        }
        is AppointmentRescheduledNotificationData -> stringResource(R.string.notification_appointment_rescheduled, data.newStartDate)
        is AppointmentReminderNotificationData -> stringResource(R.string.notification_appointment_reminder)
        is AppointmentReviewedNotificationData -> stringResource(R.string.notification_appointment_reviewed, data.rating.toInt())
        is EmploymentRequestNotificationData -> stringResource(R.string.notification_employment_request, data.professionName)
        is EmploymentRequestAcceptedNotificationData -> stringResource(R.string.notification_employment_accepted)
        is EmploymentRequestDeniedNotificationData -> stringResource(R.string.notification_employment_denied)
        is BusinessValidationNotificationData -> {
            if (data.isApproved) stringResource(R.string.notification_business_validation_approved)
            else stringResource(R.string.notification_business_validation_rejected, data.reason ?: stringResource(R.string.notification_reason_not_specified))
        }
        else -> ""
    }
}