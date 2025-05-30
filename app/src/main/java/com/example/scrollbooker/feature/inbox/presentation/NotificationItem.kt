package com.example.scrollbooker.feature.inbox.presentation

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.Avatar
import com.example.scrollbooker.core.util.Dimens.AvatarSizeS
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.ScrollBookerTheme
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.labelLarge
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun NotificationItem(
    modifier: Modifier = Modifier,
    url: String,
    fullName: String,
    type: String = "follow",
    isFollow: Boolean = false
) {
    val message = when(type) {
        "follow" -> stringResource(id = R.string.startedFollowingYou)
        "like" -> stringResource(id = R.string.likedYourPost)
        else -> ""
    }

    val isFollowText = if(isFollow) stringResource(id = R.string.following) else stringResource(id = R.string.follow)

    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .then(modifier),
        headlineContent = {
            Text(
                modifier = Modifier.padding(SpacingXXS),
                style = titleMedium,
                color = OnBackground,
                text = fullName
            )
        },
        supportingContent = {
            Text(
                style = bodyMedium,
                text = message
            )
        },
        leadingContent = {
            Avatar(url = url, size = AvatarSizeS)
        },
        trailingContent = {
            if(type == "follow") {
                SuggestionChip(
                    onClick = {},
                    label = {
                        Text(
                            style = labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            text = isFollowText
                        )
                    },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = Error,
                        labelColor = OnPrimary
                    ),
                    shape = ShapeDefaults.ExtraSmall,
                    border = BorderStroke(width = 0.dp, color = Color.Transparent)
                )
            }
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
        NotificationItem(
            url = "",
            fullName = "Some User",
            type = "follow",
            isFollow = false
        )
    }
}