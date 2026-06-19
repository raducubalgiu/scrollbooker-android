package com.example.scrollbooker.ui.booking.specialists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R

@Composable
fun UnselectedSpecialistOverlay() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .background(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.02f),
                shape = RoundedCornerShape(16.dp)
            )
            .drawBehind {
                val strokeWidth = 2.dp.toPx()
                val cornerRadius = 16.dp.toPx()

                val dashEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

                drawRoundRect(
                    color = Color.Gray,
                    style = Stroke(
                        width = strokeWidth,
                        pathEffect = dashEffect
                    ),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )
            }
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.chooseSpecialistDescription),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}