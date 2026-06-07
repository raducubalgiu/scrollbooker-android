package com.example.scrollbooker.entity.user.notification.data.mappers

import com.example.scrollbooker.core.enums.NotificationTypeEnum
import com.example.scrollbooker.entity.user.notification.data.remote.AppointmentBookedNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.AppointmentCanceledNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.AppointmentReminderNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.AppointmentRescheduledNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.AppointmentReviewedNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.BusinessValidationNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.CommentPostNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.EmploymentRequestAcceptedNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.EmploymentRequestDeniedNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.EmploymentRequestNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.FollowNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.LikePostNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.MentionPostNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.NotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.NotificationDto
import com.example.scrollbooker.entity.user.notification.data.remote.RepostNotificationDataDto
import com.example.scrollbooker.entity.user.notification.domain.model.AppointmentBookedNotificationData
import com.example.scrollbooker.entity.user.notification.domain.model.AppointmentCanceledNotificationData
import com.example.scrollbooker.entity.user.notification.domain.model.AppointmentReminderNotificationData
import com.example.scrollbooker.entity.user.notification.domain.model.AppointmentRescheduledNotificationData
import com.example.scrollbooker.entity.user.notification.domain.model.AppointmentReviewedNotificationData
import com.example.scrollbooker.entity.user.notification.domain.model.BusinessValidationNotificationData
import com.example.scrollbooker.entity.user.notification.domain.model.CommentPostNotificationData
import com.example.scrollbooker.entity.user.notification.domain.model.EmploymentRequestAcceptedNotificationData
import com.example.scrollbooker.entity.user.notification.domain.model.EmploymentRequestDeniedNotificationData
import com.example.scrollbooker.entity.user.notification.domain.model.EmploymentRequestNotificationData
import com.example.scrollbooker.entity.user.notification.domain.model.FollowNotificationData
import com.example.scrollbooker.entity.user.notification.domain.model.LikePostNotificationData
import com.example.scrollbooker.entity.user.notification.domain.model.MentionPostNotificationData
import com.example.scrollbooker.entity.user.notification.domain.model.Notification
import com.example.scrollbooker.entity.user.notification.domain.model.NotificationData
import com.example.scrollbooker.entity.user.notification.domain.model.RepostNotificationData
import com.example.scrollbooker.entity.user.userSocial.data.mappers.toDomain

fun NotificationDto.toDomain(): Notification {
    return Notification(
        id = id,
        type = NotificationTypeEnum.fromKey(type),
        senderId = senderId,
        receiverId = receiverId,
        data = data?.toDomain(),
        message = message,
        isRead = isRead,
        isDeleted = isDeleted,
        sender = sender.toDomain(),
        isFollow = isFollow
    )
}

fun NotificationDataDto.toDomain(): NotificationData {
    return when (this) {
        is FollowNotificationDataDto -> FollowNotificationData

        is LikePostNotificationDataDto -> LikePostNotificationData(
            postId = postId,
            actorIds = actorIds,
            postUrl = postUrl,
            totalCount = totalCount
        )
        is CommentPostNotificationDataDto -> CommentPostNotificationData(
            postId = postId,
            actorIds = actorIds,
            postUrl = postUrl,
            totalCount = totalCount
        )
        is RepostNotificationDataDto -> RepostNotificationData(
            postId = postId,
            actorIds = actorIds,
            postUrl = postUrl,
            totalCount = totalCount
        )
        is MentionPostNotificationDataDto -> MentionPostNotificationData(
            postId = postId,
            commentId = commentId
        )
        is AppointmentBookedNotificationDataDto -> AppointmentBookedNotificationData(
            appointmentId = appointmentId,
            customerId = customerId,
            startDate = startDate,
            customerFullName = customerFullName
        )
        is AppointmentCanceledNotificationDataDto -> AppointmentCanceledNotificationData(
            appointmentId = appointmentId,
            canceledByUserId = canceledByUserId,
            canceledReason = canceledReason
        )
        is AppointmentRescheduledNotificationDataDto -> AppointmentRescheduledNotificationData(
            appointmentId = appointmentId,
            oldStartDate = oldStartDate,
            newStartDate = newStartDate
        )
        is AppointmentReminderNotificationDataDto -> AppointmentReminderNotificationData(
            appointmentId = appointmentId,
            startDate = startDate
        )
        is AppointmentReviewedNotificationDataDto -> AppointmentReviewedNotificationData(
            appointmentId = appointmentId,
            reviewId = reviewId,
            rating = rating
        )
        is EmploymentRequestNotificationDataDto -> EmploymentRequestNotificationData(
            employmentRequestId = employmentRequestId,
            professionId = professionId,
            professionName = professionName,
            businessId = businessId
        )
        is EmploymentRequestAcceptedNotificationDataDto -> EmploymentRequestAcceptedNotificationData(
            employmentRequestId = employmentRequestId,
            businessId = businessId
        )
        is EmploymentRequestDeniedNotificationDataDto -> EmploymentRequestDeniedNotificationData(
            employmentRequestId = employmentRequestId,
            businessId = businessId
        )
        is BusinessValidationNotificationDataDto -> BusinessValidationNotificationData(
            businessId = businessId,
            isApproved = isApproved,
            reason = reason
        )
        else -> throw IllegalArgumentException("Tip necunoscut")
    }
}
