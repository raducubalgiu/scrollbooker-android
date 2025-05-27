package com.example.scrollbooker.components.typography

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.titleLarge

@Composable
fun TitleLarge(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(modifier = Modifier
        .padding(vertical = BasePadding)
        .then(modifier),
        text = text,
        style = titleLarge,
        color = OnBackground,
        fontWeight = FontWeight.Bold
    )
}