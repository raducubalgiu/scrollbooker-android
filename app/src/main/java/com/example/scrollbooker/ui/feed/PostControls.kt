package com.example.scrollbooker.ui.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.scrollbooker.R

@Composable
fun PostControls() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(5f),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(75.dp),
            painter = painterResource(R.drawable.ic_play_solid),
            contentDescription = null,
            tint = Color.White.copy(0.5f)
        )
    }
}