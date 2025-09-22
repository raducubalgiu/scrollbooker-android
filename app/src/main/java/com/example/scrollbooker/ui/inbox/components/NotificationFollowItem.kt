package com.example.scrollbooker.ui.inbox.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.buttons.MainButtonSmall
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.entity.user.notification.domain.model.Notification
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun NotificationFollowItem(
    modifier: Modifier = Modifier,
    isFollow: Boolean?,
    notification: Notification,
    enabled: Boolean,
    onFollow: (Boolean) -> Unit,
    onNavigateUserProfile: (Int) -> Unit
) {
    val isFollowed = isFollow ?: notification.isFollow
    val interactionSource = remember { MutableInteractionSource() }

    ListItem(modifier = modifier
        .fillMaxWidth()
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = { onNavigateUserProfile(notification.senderId) }
        )
        .then(modifier),
        headlineContent = {
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = SpacingXXS),
                style = titleMedium,
                color = OnBackground,
                text = notification.sender.fullName ?: ""
            )
        },
        supportingContent = {
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = bodyMedium,
                text = stringResource(id = R.string.startedFollowingYou)
            )
        },
        trailingContent = {
            MainButtonSmall(
                title = stringResource(if(isFollowed) R.string.following else R.string.follow),
                modifier = Modifier.width(110.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = if(isFollowed) Divider else Primary
                ),
                enabled = enabled,
                onClick = { onFollow(isFollowed) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if(isFollowed) Color.Transparent else Primary,
                    contentColor = if(isFollowed) OnBackground else OnPrimary
                )
            )
        },
        leadingContent = {
            Avatar(
                url = notification.sender.avatar ?: "",
                size = 60.dp
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = Background
        )
    )
}