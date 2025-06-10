package com.example.scrollbooker.components
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
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    title: String,
    colors: ButtonColors = ButtonDefaults.buttonColors()
) {
    Button(
        modifier = Modifier.fillMaxWidth().then(modifier),
        onClick = onClick,
        enabled = isEnabled,
        colors = colors
    ) {
        if(isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                strokeWidth = 2.dp,
                color = Divider,
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