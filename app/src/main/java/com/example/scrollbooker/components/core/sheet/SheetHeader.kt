package com.example.scrollbooker.components.core.sheet
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun SheetHeader(
    modifier: Modifier = Modifier,
    title: String? = "",
    customTitle: (@Composable () -> Unit)? = null,
    onClose: () -> Unit
) {
    Row(modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CustomIconButton(
            boxSize = 60.dp,
            imageVector = Icons.Default.Close,
            tint = Color.Transparent,
            onClick = {}
        )

        if(customTitle != null) {
            customTitle()
        } else {
            Text(
                text = title ?: "",
                style = titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        CustomIconButton(
            boxSize = 60.dp,
            imageVector = Icons.Default.Close,
            onClick = onClose
        )
    }
}