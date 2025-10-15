package com.example.scrollbooker.ui.shared.posts.components.postOverlay.userSection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.core.util.Dimens.AvatarSizeXXS
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun PostBusinessOrEmployeeLabel(
    fullName: String,
    avatar: String?,
    onNavigateToUser: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Icon(
        imageVector = Icons.Default.Repeat,
        contentDescription = "Repeat Icon",
        tint = Primary
    )

    Spacer(Modifier.width(SpacingM))

    Row(
        modifier = Modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onNavigateToUser
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(
            url = avatar ?: "",
            size = AvatarSizeXXS
        )

        Spacer(Modifier.width(SpacingM))

        Text(
            text = fullName,
            style = bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            fontSize = 16.sp
        )
    }
}