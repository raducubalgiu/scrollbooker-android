package com.example.scrollbooker.components.core

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.labelLarge

@Composable
fun MainButtonSmall(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    title: String
) {
    SuggestionChip(
        modifier = modifier.then(modifier),
        onClick = onClick,
        label = {
            Text(
                style = labelLarge,
                text = title,
                fontWeight = FontWeight.SemiBold
            )
        },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = Primary,
            labelColor = OnPrimary
        ),
        shape = ShapeDefaults.ExtraSmall,
        border = BorderStroke(width = 0.dp, color = Color.Transparent)
    )
}