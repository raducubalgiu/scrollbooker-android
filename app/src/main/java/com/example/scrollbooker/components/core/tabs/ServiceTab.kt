package com.example.scrollbooker.components.core.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun ServiceTab(
    isSelected: Boolean,
    serviceName: String,
    onClick: () -> Unit,
    shape: Shape = ShapeDefaults.ExtraLarge,
    paddingValues: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    style: TextStyle = bodyLarge,
    fontSize: TextUnit = 14.sp
) {
    Box(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clip(shape)
            .background(if (isSelected) SurfaceBG else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = serviceName,
            style = style,
            fontSize = fontSize,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
            color = if (isSelected) OnSurfaceBG else Color.Gray
        )
    }
}
