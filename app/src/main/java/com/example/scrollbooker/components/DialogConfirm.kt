package com.example.scrollbooker.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R

@Composable
fun DialogConfirm(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    title: String,
    text: String,
) {
    AlertDialog(
        title = {
            Text(
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                text = title
            )
        },
        text = {
            Text(
                style = MaterialTheme.typography.labelLarge,
                text = text
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.ExtraBold,
                    text = "Sterge"
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    )
}