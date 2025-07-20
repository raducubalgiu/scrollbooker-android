package com.example.scrollbooker.components.core.tabs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun StyledTab(
    onClick: () -> Unit,
    title: String,
    isSelected: Boolean
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 16.dp,
                    horizontal = 14.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                style = bodyLarge,
                fontSize = 16.sp,
                color = if (isSelected) OnSurfaceBG else Color.Gray,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}