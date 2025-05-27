package com.example.scrollbooker.feature.feed.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun CustomTab(
    onClick: () -> Unit,
    title: String,
    color: Color
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = SpacingS)
        .background(Color.Transparent)
        .clickable(onClick = onClick),
    ) {
        Text(
            text = title,
            style = titleMedium,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}