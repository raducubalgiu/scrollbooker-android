package com.example.scrollbooker.ui.auth.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.headlineLarge

@Composable
fun AuthHeader(
    headLine: String,
    subHeadline: String
) {
    Text(
        style = headlineLarge,
        color = OnBackground,
        fontWeight = FontWeight.ExtraBold,
        text = headLine
    )

    Text(
        text = subHeadline,
        color = Color.Gray
    )

    Spacer(Modifier.height(BasePadding))
}