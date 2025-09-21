package com.example.scrollbooker.ui.social.components

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
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.components.core.buttons.MainButtonSmall
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun UserSocialItem(
    modifier: Modifier = Modifier,
    isFollowedOverrides: Boolean?,
    userSocial: UserSocial,
    enabled: Boolean,
    onFollow: (Boolean) -> Unit,
    onNavigateUserProfile: (Int) -> Unit
) {
    val isFollowed = isFollowedOverrides ?: userSocial.isFollow
    val isBusinessOrEmployee = userSocial.isBusinessOrEmployee == true
    val displayProfession = isBusinessOrEmployee && userSocial.profession != null

    val interactionSource = remember { MutableInteractionSource() }

    ListItem(modifier = modifier
        .fillMaxWidth()
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = { onNavigateUserProfile(userSocial.id) }
        )
        .then(modifier),
        headlineContent = {
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = SpacingXXS),
                style = titleMedium,
                color = OnBackground,
                text = userSocial.fullName
            )
        },
        supportingContent = {
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = bodyMedium,
                text = if(displayProfession) userSocial.profession
                    else "@${userSocial.username}"
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
            if(isBusinessOrEmployee) {
                AvatarWithRating(rating = 4.5f, size = 60.dp)
            } else {
                Avatar(url = userSocial.avatar ?: "", size = 60.dp)
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = Background
        )
    )
}