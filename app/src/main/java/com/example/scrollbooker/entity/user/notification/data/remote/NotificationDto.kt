package com.example.scrollbooker.entity.user.notification.data.remote

import com.example.scrollbooker.entity.user.notification.domain.model.NotificationData
import com.example.scrollbooker.entity.user.userSocial.data.remote.UserSocialDto
import com.google.gson.annotations.SerializedName

interface NotificationDataDto

data class NotificationDto(
    val id: Int,
    val type: String,

    @SerializedName("sender_id")
    val senderId: Int,

    @SerializedName("receiver_id")
    val receiverId: Int,

    val data: NotificationDataDto?,
    val message: String?,

    @SerializedName("is_read")
    val isRead: Boolean,

    @SerializedName("is_deleted")
    val isDeleted: Boolean,

    val sender: UserSocialDto,

    @SerializedName("is_follow")
    val isFollow: Boolean
)

object FollowNotificationDataDto : NotificationDataDto

data class LikePostNotificationDataDto(
    @SerializedName("post_id")
    val postId: Int,

    @SerializedName("actor_ids")
    val actorIds: List<Int>,

    @SerializedName("post_url")
    val postUrl: String?,

    @SerializedName("total_count")
    val totalCount: Int
) : NotificationDataDto

data class CommentPostNotificationDataDto(
    @SerializedName("post_id")
    val postId: Int,

    @SerializedName("actor_ids")
    val actorIds: List<Int>,

    @SerializedName("post_url")
    val postUrl: String?,

    @SerializedName("total_count")
    val totalCount: Int
) : NotificationDataDto

data class RepostNotificationDataDto(
    @SerializedName("post_id")
    val postId: Int,

    @SerializedName("actor_ids")
    val actorIds: List<Int>,

    @SerializedName("post_url")
    val postUrl: String?,

    @SerializedName("total_count")
    val totalCount: Int
) : NotificationDataDto

data class MentionPostNotificationDataDto(
    @SerializedName("post_id")
    val postId: Int,

    @SerializedName("comment_id")
    val commentId: Int?
) : NotificationDataDto

data class AppointmentBookedNotificationDataDto(
    @SerializedName("appointment_id")
    val appointmentId: Int,

    @SerializedName("customer_id")
    val customerId: Int,

    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("customer_fullname")
    val customerFullName: String
) : NotificationDataDto

data class AppointmentCanceledNotificationDataDto(
    @SerializedName("appointment_id")
    val appointmentId: Int,

    @SerializedName("canceled_by_user_id")
    val canceledByUserId: Int,

    @SerializedName("canceled_reason")
    val canceledReason: String
) : NotificationDataDto

data class AppointmentRescheduledNotificationDataDto(
    @SerializedName("appointment_id")
    val appointmentId: Int,

    @SerializedName("old_start_date")
    val oldStartDate: String,

    @SerializedName("new_start_date")
    val newStartDate: Int
) : NotificationDataDto

data class AppointmentReminderNotificationDataDto(
    @SerializedName("appointment_id")
    val appointmentId: Int,

    @SerializedName("start_date")
    val startDate: Int
) : NotificationDataDto

data class AppointmentReviewedNotificationDataDto(
    @SerializedName("appointment_id")
    val appointmentId: Int,

    @SerializedName("review_id")
    val reviewId: Int,

    @SerializedName("rating")
    val rating: Int
) : NotificationDataDto

data class EmploymentRequestNotificationDataDto(
    @SerializedName("employment_request_id")
    val employmentRequestId: Int,

    @SerializedName("profession_id")
    val professionId: Int,

    @SerializedName("profession_name")
    val professionName: String,

    @SerializedName("business_id")
    val businessId: Int
) : NotificationDataDto

data class EmploymentRequestAcceptedNotificationDataDto(
    @SerializedName("employment_request_id")
    val employmentRequestId: Int,

    @SerializedName("business_id")
    val businessId: Int
) : NotificationDataDto

data class EmploymentRequestDeniedNotificationDataDto(
    @SerializedName("employment_request_id")
    val employmentRequestId: Int,

    @SerializedName("business_id")
    val businessId: Int
) : NotificationDataDto

data class BusinessValidationNotificationDataDto(
    @SerializedName("business_id")
    val businessId: Int,

    @SerializedName("is_approved")
    val isApproved: Boolean,

    @SerializedName("reason")
    val reason: String?,
) : NotificationDataDto