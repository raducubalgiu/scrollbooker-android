package com.example.scrollbooker.components.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnError

@Composable
fun CustomBadge(
    modifier: Modifier = Modifier,
    content: Int,
    containerColor: Color = Error,
    contentColor: Color = OnError
) {
    Box(
        modifier = Modifier
            .background(containerColor, shape = RoundedCornerShape(50))
            .padding(
                horizontal = 6.dp,
                vertical = 0.dp
            )
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = content.toString(),
            color = contentColor,
            fontSize = 13.sp,
            maxLines = 1,
            lineHeight = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}