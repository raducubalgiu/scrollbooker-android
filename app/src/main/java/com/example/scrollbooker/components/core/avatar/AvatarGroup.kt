package com.example.scrollbooker.components.core.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.scrollbooker.core.util.Dimens.AvatarSizeXXS
import com.example.scrollbooker.ui.theme.Background

@Composable
fun AvatarGroup(
    avatarUrls: List<String>,
    modifier: Modifier = Modifier,
    size: Dp = AvatarSizeXXS,
    overlap: Dp = 7.dp,
    borderWidth: Dp = 2.dp,
    borderColor: Color = Background
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(-overlap)
    ) {
        avatarUrls.forEachIndexed { index, url ->
            Box(
                modifier = Modifier
                    .zIndex(index.toFloat())
                    .size(size + borderWidth * 2)
                    .clip(CircleShape)
                    .background(borderColor),
                contentAlignment = Alignment.Center
            ) {
                Avatar(url = url, size = size)
            }
        }
    }
}
