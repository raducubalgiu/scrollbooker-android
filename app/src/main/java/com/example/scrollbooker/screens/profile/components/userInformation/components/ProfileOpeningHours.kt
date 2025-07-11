package com.example.scrollbooker.screens.profile.components.userInformation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.formatOpeningHours
import com.example.scrollbooker.entity.user.userProfile.domain.model.OpeningHours
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun ProfileOpeningHours(
    onOpenScheduleSheet: () -> Unit,
    openingHours: OpeningHours
) {
    Spacer(Modifier.height(SpacingXS))
    val interactionSource = remember { MutableInteractionSource() }

    Row(modifier = Modifier
        .clickable(
            onClick = onOpenScheduleSheet,
            interactionSource = interactionSource,
            indication = null
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Schedule,
            contentDescription = null,
            tint = OnSurfaceBG
        )
        Spacer(Modifier.width(5.dp))
        Text(
            text = formatOpeningHours(openingHours).toString(),
            style = bodyMedium,
            color = OnSurfaceBG,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.width(5.dp))
        Icon(
            imageVector = Icons.Outlined.KeyboardArrowDown,
            contentDescription = null,
            tint = OnSurfaceBG
        )
    }
}