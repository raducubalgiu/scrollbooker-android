package com.example.scrollbooker.ui.profile.components.profileHeader.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.OnBackground

@Composable
fun ProfileBusinessEmployee(
    businessOwnerAvatar: String?,
    businessOwnerFullName: String?,
    onNavigateToBusinessOwner: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = BasePadding)
        .clickable(
            onClick = { onNavigateToBusinessOwner() },
            interactionSource = interactionSource,
            indication = null
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Repeat,
            contentDescription = null,
            tint = Color.Gray
        )
        Spacer(Modifier.width(SpacingS))
        Avatar(
            url = businessOwnerAvatar ?: "",
            size = 25.dp
        )
        Spacer(Modifier.width(SpacingS))
        Text(
            text = businessOwnerFullName ?: "",
            color = OnBackground,
            fontWeight = FontWeight.SemiBold
        )
    }
}