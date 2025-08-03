package com.example.scrollbooker.ui.profile.components.profileHeader.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Primary

@Composable
fun ProfileIntentActionButton(
    icon: Painter,
    title: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = icon,
            contentDescription = null,
            tint = Primary.copy(alpha = 0.8f)
        )
        Spacer(Modifier.width(SpacingS))
        Text(
            text = title,
            fontWeight = FontWeight.Bold
        )
    }
}