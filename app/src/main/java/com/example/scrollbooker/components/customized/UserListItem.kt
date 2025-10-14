package com.example.scrollbooker.components.customized
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
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
    rating: Float?,
    isEnabled: Boolean,
    isBusinessOrEmployee: Boolean,
    onNavigateUserProfile: () -> Unit,
    trailingContent: @Composable (() -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }

    ListItem(modifier = modifier
        .fillMaxWidth()
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = {
                if (isEnabled) {
                    onNavigateUserProfile()
                }
            }
        )
        .then(modifier),
        headlineContent = {
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = SpacingXXS),
                style = titleMedium,
                color = OnBackground,
                text = title
            )
        },
        supportingContent = {
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = bodyMedium,
                text = description
            )
        },
        trailingContent = {
            trailingContent?.invoke()
        },
        leadingContent = {
            if(isBusinessOrEmployee && rating != null) {
                AvatarWithRating(
                    url = avatar,
                    rating = rating,
                    size = 60.dp,
                    onClick = {
                        if (isEnabled) {
                            onNavigateUserProfile()
                        }
                    }
                )
            } else {
                Avatar(url = avatar, size = 60.dp)
            }
        },
        colors = ListItemDefaults.colors(containerColor = Background)
    )
}