package com.example.scrollbooker.screens.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.core.nav.host.AuthTypeEnum
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun AuthFooter(
    type: AuthTypeEnum,
    onNavigate: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                color = OnBackground,
                text = if(type == AuthTypeEnum.LOGIN) stringResource(R.string.dontHaveAnAccount)
                else stringResource(R.string.alreadyHaveAnAccount)
            )
            TextButton(
                onClick = onNavigate
            ) {
                Text(
                    style = bodyLarge,
                    fontWeight = FontWeight.Bold,
                    text = stringResource(id =
                        if(type == AuthTypeEnum.LOGIN) R.string.register
                        else R.string.login
                    )
                )
            }
        }
    }
}