package com.example.scrollbooker.screens.auth.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun AuthErrorMessage(errorMessage: String) {
    Column(modifier = Modifier.padding(vertical = BasePadding)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.Warning,
                contentDescription = null,
                tint = Error
            )
            Spacer(Modifier.width(SpacingS))
            Text(
                text = errorMessage,
                color = Error,
                style = bodyMedium
            )
        }
    }
}