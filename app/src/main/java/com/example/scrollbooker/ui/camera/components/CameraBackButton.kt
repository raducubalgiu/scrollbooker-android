package com.example.scrollbooker.ui.camera.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.scrollbooker.core.util.Dimens.BasePadding

@Composable
fun CameraBackButton(onBack: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(modifier = Modifier
        .zIndex(5f)
        .clickable(
            onClick = onBack,
            interactionSource = interactionSource,
            indication = null
        )
    ) {
        Box(
            modifier = Modifier.padding(BasePadding),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Default.Close,
                contentDescription = "Back Button",
                tint = Color(0xFFE0E0E0),
            )
        }
    }
}