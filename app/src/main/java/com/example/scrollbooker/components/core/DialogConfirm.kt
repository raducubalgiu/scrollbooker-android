package com.example.scrollbooker.components.core

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun DialogConfirm(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    title: String,
    text: String,
    confirmText: String = stringResource(R.string.delete)
) {
    AlertDialog(
        title = {
            Text(
                style = titleMedium,
                color = OnSurfaceBG,
                fontWeight = FontWeight.Bold,
                text = title
            )
        },
        text = {
            Text(
                style = bodyMedium,
                text = text
            )
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirmation) {
                Text(
                    color = Error,
                    fontWeight = FontWeight.ExtraBold,
                    text = confirmText
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = OnSurfaceBG
                )
            }
        },
        containerColor = SurfaceBG
    )
}