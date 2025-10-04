package com.example.scrollbooker.ui.shared.userProducts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun ServiceTab(
    isSelected: Boolean,
    serviceName: String,
    productsCount: Int,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(modifier = Modifier
        .padding(vertical = 8.dp)
        .clip(shape = ShapeDefaults.Small)
        .background(if(isSelected) SurfaceBG else Color.Transparent)
        .clickable(
            onClick = onClick,
            interactionSource = interactionSource,
            indication = null
        )
    ) {
        Box(
            modifier = Modifier
                .padding(
                    vertical = 10.dp,
                    horizontal = 14.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$serviceName $productsCount",
                style = bodyLarge,
                fontSize = 16.sp,
                color = if (isSelected) OnSurfaceBG else Color.Gray,
                fontWeight = if(isSelected) FontWeight.Bold else FontWeight.SemiBold,
            )
        }
    }
}