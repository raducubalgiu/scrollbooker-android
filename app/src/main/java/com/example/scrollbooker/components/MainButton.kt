package com.example.scrollbooker.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun MainButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    title: String
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().then(modifier),
        enabled = enabled
    ) {
        Text(
            style = bodyMedium,
            fontWeight = FontWeight.SemiBold,
            text = title
        )
    }
}