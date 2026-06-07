package com.example.scrollbooker.ui.inbox.components
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButtonSmall
import com.example.scrollbooker.components.customized.UserListItem
import com.example.scrollbooker.core.enums.NotificationTypeEnum
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
import com.example.scrollbooker.entity.user.notification.domain.model.RepostNotificationData
import com.example.scrollbooker.navigation.navigators.InboxNavigator
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnError
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary

@Composable
fun NotificationItem(
    notification: Notification,
    isLocked: Boolean,
    isFollowed: Boolean?,
    onFollow: (Boolean) -> Unit,
    inboxNavigate: InboxNavigator,
    modifier: Modifier = Modifier
) {
    val description = when (val data = notification.data) {
        is FollowNotificationData -> stringResource(R.string.startedFollowingYou)

        is LikePostNotificationData -> {
            if (data.totalCount > 1) "a apreciat postarea ta și încă ${data.totalCount - 1} persoane"
            else "a apreciat postarea ta"
        }

        is CommentPostNotificationData -> "a lăsat un comentariu la postarea ta"
        is RepostNotificationData -> "a distribuit postarea ta"
        is MentionPostNotificationData -> "te-a menționat într-o postare"

        is AppointmentBookedNotificationData -> "A efectuat o programare pentru data de ${data.startDate}"
        is AppointmentCanceledNotificationData -> {
            if (data.canceledReason.isNotBlank()) "a anulat programarea. Motiv: ${data.canceledReason}"
            else "a anulat programarea."
        }
        is AppointmentRescheduledNotificationData -> "Programarea a fost replanificată pe ${data.newStartDate}"
        is AppointmentReminderNotificationData -> "Memento: Ai o programare stabilită în curând."
        is AppointmentReviewedNotificationData -> "A lăsat o recenzie de ${data.rating} stele pentru programarea finalizată."

        is EmploymentRequestNotificationData -> "Ai primit o cerere de angajare pentru rolul de ${data.professionName}."
        is EmploymentRequestAcceptedNotificationData -> "a acceptat cererea ta de angajare."
        is EmploymentRequestDeniedNotificationData -> "a respins cererea ta de angajare."

        is BusinessValidationNotificationData -> {
            if (data.isApproved) "Afacerea ta a fost validată cu succes de administratori."
            else "Afacerea ta a fost respinsă. Motiv: ${data.reason}"
        }
        else -> ""
    }

    val trailingContent: @Composable (() -> Unit)? = when (notification.type) {
        NotificationTypeEnum.FOLLOW -> {
            {
                val activeFollow = isFollowed ?: notification.isFollow
                MainButtonSmall(
                    title = stringResource(if(activeFollow) R.string.following else R.string.follow),
                    modifier = Modifier.width(110.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if(activeFollow) Divider else Primary
                    ),
                    enabled = !isLocked,
                    onClick = { onFollow(activeFollow) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if(activeFollow) Color.Transparent else Primary,
                        contentColor = if(activeFollow) OnBackground else OnPrimary
                    ),
                    shape = ShapeDefaults.ExtraLarge
                )
            }
        }

        NotificationTypeEnum.EMPLOYMENT_REQUEST -> {
            {
                MainButtonSmall(
                    title = stringResource(R.string.seeMore),
                    onClick = {
                        (notification.data as? EmploymentRequestNotificationData)?.employmentRequestId?.let { id ->
                            inboxNavigate.toEmploymentRespond(id)
                        }
                    },
                    colors = ButtonColors(
                        containerColor = Error,
                        contentColor = OnError,
                        disabledContainerColor = Divider,
                        disabledContentColor = OnSurfaceBG
                    ),
                    shape = ShapeDefaults.ExtraLarge
                )
            }
        }

        NotificationTypeEnum.APPOINTMENT_BOOKED,
        NotificationTypeEnum.APPOINTMENT_CANCELED,
        NotificationTypeEnum.APPOINTMENT_REVIEWED -> {
            {
                MainButtonSmall(
                    title = "Detalii",
                    shape = ShapeDefaults.ExtraLarge,
                    onClick = {
                        val appointmentId = when (val d = notification.data) {
                            is AppointmentBookedNotificationData -> d.appointmentId
                            is AppointmentCanceledNotificationData -> d.appointmentId
                            is AppointmentReviewedNotificationData -> d.appointmentId
                            else -> null
                        }
                        appointmentId?.let {  }
                    }
                )
            }
        }

        NotificationTypeEnum.APPOINTMENT_RESCHEDULED -> {
            {
                MainButtonSmall(
                    title = "Verifică",
                    shape = ShapeDefaults.ExtraLarge,
                    onClick = {
                        (notification.data as? AppointmentRescheduledNotificationData)?.appointmentId?.let { id ->

                        }
                    }
                )
            }
        }

        else -> null
    }

    UserListItem(
        modifier = modifier,
        title = notification.sender.fullName,
        description = description,
        avatar = notification.sender.avatar ?: "",
        rating = notification.sender.ratingsAverage,
        isEnabled = true,
        isBusinessOrEmployee = false,
        onNavigateUserProfile = { inboxNavigate.toUserProfile(notification.senderId) },
        trailingContent = trailingContent
    )
}
