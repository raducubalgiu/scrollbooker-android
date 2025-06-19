package com.example.scrollbooker.components.customized.listItems
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.core.util.Dimens.AvatarSizeS
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.screens.profile.userSocial.domain.model.UserSocial
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun SelectableUserItem(
    user: UserSocial,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    enablePaddingH: Boolean = true,
    onClick: () -> Unit
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable(
            onClick = onClick,
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple()
        )
        .then(modifier)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                vertical = SpacingM,
                horizontal = if(enablePaddingH) BasePadding else 0.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically
                ) {
                    Avatar("", size = AvatarSizeS)
                    Spacer(Modifier.width(SpacingM))
                    Column {
                        Text(
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(bottom = SpacingXXS),
                            style = titleMedium,
                            color = OnBackground,
                            text = user.fullName
                        )
                        Text(
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = bodyMedium,
                            text = "@${user.username}"
                        )
                    }
                }
            }

            RadioButton(
                modifier = Modifier.scale(1.3f).padding(end = BasePadding),
                selected = (isSelected),
                onClick = null,
                colors = RadioButtonColors(
                    selectedColor = Primary,
                    unselectedColor = Divider,
                    disabledSelectedColor = Divider,
                    disabledUnselectedColor = Divider
                )
            )
        }
    }
}