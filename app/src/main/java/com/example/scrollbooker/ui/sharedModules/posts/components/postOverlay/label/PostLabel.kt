package com.example.scrollbooker.ui.sharedModules.posts.components.postOverlay.label

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnError

@Composable
fun PostLabel(
    icon: Int,
    title: String,
    containerColor: Color = Error,
    contentColor: Color = OnError
) {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(containerColor.copy(alpha = 0.8f))
            .padding(vertical = SpacingXS, horizontal = SpacingS),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = contentColor
        )
        Spacer(Modifier.width(SpacingS))
        Text(
            text = title,
            color = contentColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}