package com.example.scrollbooker.components.core.headers
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun HeaderEdit(
    onBack: (() -> Unit)? = null,
    title: String,
    onAction: () -> Unit,
    actionTitle: String? = stringResource(R.string.save),
    isEnabled: Boolean = true,
    isLoading: Boolean = false
) {
    Header(
        title = title,
        onBack = onBack,
        actions = {
            TextButton(
                onClick = onAction,
                enabled = isEnabled && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Primary,
                    disabledContentColor = Divider,
                    disabledContainerColor = Color.Transparent
                ),
                interactionSource = remember { MutableInteractionSource() },
            ) {
                if(isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 3.dp
                    )
                } else {
                    Text(
                        style = titleMedium,
                        fontWeight = FontWeight.Bold,
                        text = actionTitle ?: ""
                    )
                }
            }
        }
    )
}