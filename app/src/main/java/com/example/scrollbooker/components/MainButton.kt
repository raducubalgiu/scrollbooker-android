package com.example.scrollbooker.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
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
    isLoading: Boolean = false,
    onClick: () -> Unit,
    enabled: Boolean = true,
    title: String
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().then(modifier),
        enabled = enabled
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