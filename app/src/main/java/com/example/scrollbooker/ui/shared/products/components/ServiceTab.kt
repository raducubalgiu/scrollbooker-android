package com.example.scrollbooker.ui.shared.products.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun ServiceTab(
    isSelected: Boolean,
    serviceName: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Button(
        modifier = Modifier.padding(vertical = 8.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if(isSelected) SurfaceBG else Color.Transparent,
            contentColor = if (isSelected) OnSurfaceBG else Color.Gray
        ),
        shape = ShapeDefaults.ExtraLarge,
    ) {
        Text(
            text = serviceName,
            style = bodyLarge,
            fontSize = 16.sp,
            fontWeight = if(isSelected) FontWeight.Bold else FontWeight.SemiBold,
        )
    }
}