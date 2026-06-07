package com.example.scrollbooker.ui.inbox.components
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButtonSmall
import com.example.scrollbooker.components.customized.UserListItem
import com.example.scrollbooker.core.enums.NotificationTypeEnum
import com.example.scrollbooker.entity.user.notification.domain.model.AppointmentBookedNotificationData
import com.example.scrollbooker.entity.user.notification.domain.model.AppointmentCanceledNotificationData
import com.example.scrollbooker.entity.user.notification.domain.model.AppointmentRescheduledNotificationData
import com.example.scrollbooker.entity.user.notification.domain.model.AppointmentReviewedNotificationData
import com.example.scrollbooker.entity.user.notification.domain.model.EmploymentRequestNotificationData
import com.example.scrollbooker.entity.user.notification.domain.model.Notification
import com.example.scrollbooker.entity.user.notification.domain.model.resolveDescription
import com.example.scrollbooker.navigation.navigators.InboxNavigator
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnError
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary

data class NotificationBadgeConfig(
    val icon: ImageVector,
    val containerColor: Color
)

@Composable
fun NotificationItem(
    notification: Notification,
    isLocked: Boolean,
    isFollowed: Boolean?,
    onFollow: (Boolean) -> Unit,
    inboxNavigate: InboxNavigator,
    modifier: Modifier = Modifier
) {
    val description = notification.resolveDescription()

    val descMaxLines = remember(notification.id, notification.type) {
        if (notification.type == NotificationTypeEnum.FOLLOW) 1 else 2
    }

    val trailingContent: @Composable (() -> Unit)? = remember(notification.id, isFollowed, isLocked) {
        when (notification.type) {
            NotificationTypeEnum.FOLLOW -> {
                {
                    val activeFollow = isFollowed ?: notification.isFollow
                    MainButtonSmall(
                        title = stringResource(if (activeFollow) R.string.following else R.string.follow),
                        modifier = Modifier.width(100.dp),
                        border = BorderStroke(1.dp, if (activeFollow) Divider else Primary),
                        enabled = !isLocked,
                        onClick = { onFollow(activeFollow) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (activeFollow) Color.Transparent else Primary,
                            contentColor = if (activeFollow) OnBackground else OnPrimary
                        ),
                        shape = ShapeDefaults.ExtraLarge
                    )
                }
            }

            NotificationTypeEnum.EMPLOYMENT_REQUEST -> {
                {
                    MainButtonSmall(
                        title = stringResource(R.string.seeDetails),
                        modifier = Modifier.width(100.dp),
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
                    val appointmentId = (notification.data as? AppointmentBookedNotificationData)?.appointmentId
                        ?: (notification.data as? AppointmentCanceledNotificationData)?.appointmentId
                        ?: (notification.data as? AppointmentReviewedNotificationData)?.appointmentId

                    MainButtonSmall(
                        title = stringResource(R.string.seeDetails),
                        modifier = Modifier.width(100.dp),
                        shape = ShapeDefaults.ExtraLarge,
                        onClick = { appointmentId?.let {
                            inboxNavigate.toAppointmentDetail(it)
                        } }
                    )
                }
            }

            NotificationTypeEnum.APPOINTMENT_RESCHEDULED -> {
                {
                    val appointmentId = (notification.data as? AppointmentRescheduledNotificationData)?.appointmentId
                    MainButtonSmall(
                        title = stringResource(R.string.check),
                        modifier = Modifier.width(100.dp),
                        shape = ShapeDefaults.ExtraLarge,
                        onClick = { appointmentId?.let {
                            inboxNavigate.toAppointmentDetail(it)
                        }}
                    )
                }
            }

            else -> null
        }
    }

    val badgeConfig = rememberNotificationBadge(type = notification.type)

    UserListItem(
        modifier = modifier,
        title = notification.sender.fullName,
        description = description,
        avatar = notification.sender.avatar ?: "",
        isEnabled = true,
        titleMaxLines = 1,
        descriptionMaxLines = descMaxLines,
        onNavigateUserProfile = { inboxNavigate.toUserProfile(notification.senderId) },
        badgeConfig = badgeConfig,
        trailingContent = trailingContent
    )
}
