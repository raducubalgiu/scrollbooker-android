package com.example.scrollbooker.components.core

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun MainButton(
    modifier: Modifier = Modifier,
    fullWidth: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit,
    enabled: Boolean = true,
    title: String,
    colors: ButtonColors = ButtonDefaults.buttonColors()
) {
    Button(
        onClick = onClick,
        modifier = if(fullWidth) Modifier.fillMaxWidth().then(modifier) else modifier,
        enabled = enabled,
        colors = colors
    ) {
        if(isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(25.dp),
                strokeWidth = 4.dp,
                color = Divider
            )
        } else {
            Text(
                style = bodyMedium,
                fontWeight = FontWeight.SemiBold,
                text = title
            )
        }
    }
}