package com.example.scrollbooker.ui.shared.posts.components.postOverlay.labels

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnError
import com.example.scrollbooker.ui.theme.labelMedium

@Composable
fun PostOverlayLabel(
    icon: Int,
    title: String,
    containerColor: Color = Error,
    contentColor: Color = OnError
) {
    Row(
        modifier = Modifier
            .clip(shape = ShapeDefaults.ExtraLarge)
            .background(containerColor.copy(alpha = 0.9f))
            .padding(vertical = SpacingS, horizontal = SpacingS),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(15.dp),
            painter = painterResource(icon),
            contentDescription = null,
            tint = contentColor
        )
        Spacer(Modifier.width(SpacingS))
        Text(
            text = title,
            style = labelMedium,
            color = contentColor,
            fontWeight = FontWeight.SemiBold,
        )
    }
}