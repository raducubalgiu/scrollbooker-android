package com.example.scrollbooker.ui.shared.posts.components.postOverlay.labels

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.labelLarge

@Composable
fun VideoReviewLabel() {
    Column(
        modifier = Modifier
            .clip(shape = ShapeDefaults.ExtraSmall)
            .background(Color.Black.copy(alpha = 0.2f))
            .padding(vertical = 8.dp, horizontal = 12.dp),
    ) {
        Text(
            text = "Recenzie video",
            style = labelLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold,
        )
    }
}