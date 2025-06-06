package com.example.scrollbooker.feature.userSocial.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Avatar
import com.example.scrollbooker.components.core.MainButtonSmall
import com.example.scrollbooker.core.util.Dimens.AvatarSizeS
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.feature.userSocial.domain.model.UserSocial
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.ScrollBookerTheme
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun UserSocialItem(
    modifier: Modifier = Modifier,
    isFollowedOverrides: Boolean?,
    userSocial: UserSocial,
    enabled: Boolean,
    onFollow: (Boolean) -> Unit
) {
    val isFollowed = isFollowedOverrides ?: userSocial.isFollow

    ListItem(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = SpacingXXS)
        .width(72.dp)
        .then(modifier),
        headlineContent = {
            Text(
                modifier = Modifier.padding(bottom = SpacingXXS),
                style = titleMedium,
                color = OnBackground,
                text = userSocial.username
            )
        },
        supportingContent = {
            Text(
                style = bodyMedium,
                text = userSocial.fullName
            )
        },
        trailingContent = {
            MainButtonSmall(
                border = BorderStroke(width = 1.dp, color = if(isFollowed) Divider else Primary),
                title = stringResource(if(isFollowed) R.string.following else R.string.follow),
                enabled = enabled,
                onClick = { onFollow(isFollowed) },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = if(isFollowed) Color.Transparent else Primary,
                    labelColor = if(isFollowed) OnBackground else OnPrimary
                )
            )
        },
        leadingContent = {
            Avatar(url = userSocial.avatar ?: "", size = AvatarSizeS)
        },
        colors = ListItemDefaults.colors(
            containerColor = Background
        )
    )
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NotificationItemPreview() {
    ScrollBookerTheme() {
        UserSocialItem(
            userSocial = UserSocial(
                id = 1,
                fullName = "John Doe",
                username = "john_doe",
                avatar = "",
                isFollow = true
            ),
            isFollowedOverrides = true,
            enabled = true,
            onFollow = {}
        )
    }
}