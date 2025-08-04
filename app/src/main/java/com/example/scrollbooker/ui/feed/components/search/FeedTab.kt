package com.example.scrollbooker.ui.feed.components.search

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Primary

@Composable
fun FeedTab(
    isSelected: Boolean,
    onClick: () -> Unit,
    title: String
) {
    val interactionSource = remember { MutableInteractionSource() }

    val animatedScale by animateFloatAsState(
        targetValue = if(isSelected) 1.05f else 1f,
        animationSpec = tween(durationMillis = 300)
    )

    Box(modifier = Modifier
        .clip(shape = ShapeDefaults.ExtraLarge)
        .background(if (isSelected) Primary.copy(alpha = 0.6f) else Color.Transparent)
        .padding(
            vertical = 9.dp,
            horizontal = SpacingS
        )
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.scale(animatedScale),
            text = title,
            color = if (isSelected) Color.White else Color.White.copy(0.9f),
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.8f),
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}