package com.example.scrollbooker.components.customized
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.ui.inbox.components.NotificationBadgeConfig
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun UserListItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    avatar: String,
    isEnabled: Boolean,
    titleMaxLines: Int = 1,
    descriptionMaxLines: Int = 2,
    badgeConfig: NotificationBadgeConfig? = null,
    onNavigateUserProfile: () -> Unit,
    trailingContent: @Composable (() -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }

    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { if (isEnabled) onNavigateUserProfile() }
            ),
        headlineContent = {
            Text(
                text = title,
                maxLines = titleMaxLines,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = SpacingXXS),
                style = titleMedium,
                color = OnBackground
            )
        },
        supportingContent = {
            Text(
                text = description,
                maxLines = descriptionMaxLines,
                overflow = TextOverflow.Ellipsis,
                style = bodyMedium
            )
        },
        trailingContent = trailingContent,
        leadingContent = {
            Box(
                modifier = Modifier.wrapContentSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Avatar(url = avatar, size = 48.dp)

                if (badgeConfig != null) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .offset(x = 2.dp, y = 2.dp)
                            .clip(CircleShape)
                            .background(Background)
                            .padding(1.5.dp)
                            .clip(CircleShape)
                            .background(badgeConfig.containerColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = badgeConfig.icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(11.dp)
                        )
                    }
                }
            }
        },
        colors = ListItemDefaults.colors(containerColor = Background)
    )
}