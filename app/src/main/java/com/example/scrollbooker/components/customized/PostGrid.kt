package com.example.scrollbooker.components.customized

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun PostGrid() {
    Box(modifier = Modifier
        .aspectRatio(9f / 11f)
        .border(1.dp, Divider)
        .background(SurfaceBG)
    ) {
        Icon(
            imageVector = Icons.Default.Image,
            contentDescription = "Post",
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.Center),
            tint = Color.DarkGray
        )
    }
}