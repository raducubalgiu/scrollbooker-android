package com.example.scrollbooker.screens.feed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Error

@Composable
fun CustomTab(
    onClick: () -> Unit,
    title: String,
    color: Color
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = SpacingS)
        .background(Color.Transparent)
        .clickable(onClick = onClick),
    ) {
        Text(
            text = title,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.8f),
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            ),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = color,
        )
    }
}