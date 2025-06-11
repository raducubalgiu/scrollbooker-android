package com.example.scrollbooker.feature.notifications.presentation.components
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.scrollbooker.components.core.Avatar
import com.example.scrollbooker.components.core.MainButtonSmall
import com.example.scrollbooker.core.util.Dimens.AvatarSizeS
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.ScrollBookerTheme
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun NotificationItem(
    modifier: Modifier = Modifier,
    fullName: String,
    message: String,
    avatar: String,
    actionModifier: Modifier = Modifier,
    showAction: Boolean = true,
    actionTitle: String = "",
    onActionClick: (() -> Unit)? = null
) {
    ListItem(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = SpacingXXS)
        .then(modifier),
        headlineContent = {
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(bottom = SpacingXXS),
                style = titleMedium,
                color = OnBackground,
                text = fullName
            )
        },
        supportingContent = {
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = bodyMedium,
                text = message
            )
        },
        trailingContent = {
            if(showAction) {
                Column {
                    MainButtonSmall(
                        modifier = actionModifier,
                        title = actionTitle,
                        onClick = { onActionClick }
                    )
                }
            }
        },
        leadingContent = {
            Avatar(url = avatar, size = AvatarSizeS)
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
            fullName = "Joh Doe",
            message = "a inceput sa te urmareasca",
            avatar = "",
            actionModifier = TODO(),
            actionTitle = "Urmareste",
        )
    }
}