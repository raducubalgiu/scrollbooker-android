package com.example.scrollbooker.ui.inbox.components
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.buttons.MainButtonSmall
import com.example.scrollbooker.core.util.Dimens.AvatarSizeS
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
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
    actionBackgroundColor: Color = Primary,
    actionColor: Color = OnPrimary,
    onActionClick: (() -> Unit)? = null
) {
    ListItem(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = SpacingXXS)
        .clickable {

        }
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
                        onClick = { onActionClick?.invoke() },
                        colors = ButtonColors(
                            containerColor = actionBackgroundColor,
                            contentColor = actionColor,
                            disabledContainerColor = Divider,
                            disabledContentColor = OnSurfaceBG
                        ),
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