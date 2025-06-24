package com.example.scrollbooker.components.core.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ShapeDefaults
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
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Primary,
        contentColor = OnPrimary
    )
) {
    Button(
        modifier = modifier
            .width(96.dp)
            .height(32.dp),
        onClick = onClick,
        contentPadding = PaddingValues(
            vertical = 6.dp,
            horizontal = 14.dp
        ),
        shape = ShapeDefaults.ExtraSmall,
        enabled = enabled,
        border = border,
        colors = colors,
    ) {
        Text(
            style = labelLarge,
            text = title,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}