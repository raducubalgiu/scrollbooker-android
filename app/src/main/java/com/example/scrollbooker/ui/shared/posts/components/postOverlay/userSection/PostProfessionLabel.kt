package com.example.scrollbooker.ui.shared.posts.components.postOverlay.userSection

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.ui.theme.Primary

@Composable
fun PostProfessionLabel(profession: String) {
    Text(
        text = profession,
        style = TextStyle(
            shadow = Shadow(
                color = Color.Black.copy(alpha = 0.6f),
                offset = Offset(1f, 1f),
                blurRadius = 3f
            ),
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        fontWeight = FontWeight.SemiBold,
        color = Primary.copy(alpha = 0.85f)
    )
}