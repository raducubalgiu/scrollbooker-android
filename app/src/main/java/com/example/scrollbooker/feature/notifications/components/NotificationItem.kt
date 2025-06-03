package com.example.scrollbooker.feature.notifications.components
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.Avatar
import com.example.scrollbooker.components.core.MainButtonSmall
import com.example.scrollbooker.core.util.Dimens.AvatarSizeS
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun NotificationItem(
    modifier: Modifier = Modifier,
    fullName: String,
    message: String,
    avatar: String,
    trailingContent: (@Composable () -> Unit)? = null,
    trailingTitle: String = "",
    onTrailingClick: (() -> Unit)? = null
) {
    ListItem(modifier = Modifier
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
        trailingContent = {
            if(trailingContent != null) {
                MainButtonSmall(
                    title = trailingTitle,
                    onClick = { onTrailingClick }
                )
            } else {
                trailingContent?.invoke()
            }
        },
        leadingContent = {
            Avatar(url = avatar, size = AvatarSizeS)
        },
        colors = ListItemDefaults.colors(
            containerColor = Background
        )
    )

    Spacer(
        Modifier
            .height(BasePadding)
            .background(Background),
    )
}