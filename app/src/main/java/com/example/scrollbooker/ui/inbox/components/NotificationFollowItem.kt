package com.example.scrollbooker.ui.inbox.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
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
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium
import timber.log.Timber

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

    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.StartToEnd) {
                Timber.e("START TO END!!!")
            }
            else if (it == SwipeToDismissBoxValue.EndToStart) {
                Timber.e("END TO START!!!")
            }
            // Reset item when toggling done status
            it != SwipeToDismissBoxValue.StartToEnd
        }
    )

    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        modifier = modifier.fillMaxSize(),
        backgroundContent = {
            when (swipeToDismissBoxState.dismissDirection) {
                SwipeToDismissBoxValue.EndToStart -> {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove item",
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                lerp(
                                    Color.LightGray,
                                    Color.Red,
                                    swipeToDismissBoxState.progress
                                )
                            )
                            .wrapContentSize(Alignment.CenterEnd)
                            .padding(12.dp),
                        tint = Color.White
                    )
                }
                else -> {}
            }
        }
    ) {
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
                    ),
                    shape = ShapeDefaults.ExtraLarge
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
}