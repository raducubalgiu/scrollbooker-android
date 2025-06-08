package com.example.scrollbooker.components.core

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ChipColors
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.labelLarge

@Composable
fun MainButtonSmall(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    title: String,
    enabled: Boolean = true,
    border: BorderStroke = BorderStroke(width = 0.dp, color = Color.Transparent),
    colors: ChipColors = SuggestionChipDefaults.suggestionChipColors(
        containerColor = Primary,
        labelColor = OnPrimary
    )
) {
    SuggestionChip(
        modifier = modifier.then(modifier),
        onClick = onClick,
        label = {
            Text(
                style = labelLarge,
                text = title,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        enabled = enabled,
        colors = colors,
        shape = ShapeDefaults.ExtraSmall,
        border = border
    )
}