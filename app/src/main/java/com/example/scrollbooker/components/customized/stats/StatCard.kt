package com.example.scrollbooker.components.customized.stats

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleLarge

@Composable
fun StatCard(
    modifier: Modifier,
    label: String,
    value: String,
    containerColor: Color = SurfaceBG,
    contentColor: Color = OnSurfaceBG,
    borderColor: Color? = null,
    gradientColors: List<Color>? = null
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (gradientColors != null) Color.Transparent else containerColor
        ),
        shape = ShapeDefaults.Small,
        border = if (borderColor != null) {
            BorderStroke(0.55.dp, borderColor)
        } else {
            null
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(ShapeDefaults.Small)
                .then(
                    if (gradientColors != null) {
                        Modifier.background(Brush.linearGradient(gradientColors))
                    } else {
                        Modifier
                    }
                )
                .padding(16.dp)
        ) {
            Text(
                text = label,
                style = bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = contentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = titleLarge,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
    }
}