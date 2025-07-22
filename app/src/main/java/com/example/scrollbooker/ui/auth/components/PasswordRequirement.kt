package com.example.scrollbooker.ui.auth.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Error

@Composable
fun PasswordRequirement(password: String) {
    Column {
        RequirementItem(
            stringResource(R.string.passwordShouldContainMinimumEightCharacters),
            password.length > 8
        )
        RequirementItem(
            stringResource(R.string.passwordShouldContainMaximumTwentyCharacters),
            password.length < 20
        )
        RequirementItem(
            stringResource(R.string.passwordShouldContainAtLeastOneBigLetter),
            password.any { it.isUpperCase() }
        )
        RequirementItem(
            stringResource(R.string.passwordShouldContainAtLeastOneDigit),
            password.any { it.isDigit() }
        )
    }
}

@Composable
fun RequirementItem(text: String, fulfilled: Boolean) {
    val icon = if(fulfilled) Icons.Default.Check else Icons.Default.Close
    val color = if(fulfilled) Color.Green else Error

    Row(
        modifier = Modifier.padding(top = SpacingS),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            tint = color,
            contentDescription = null
        )
        Spacer(Modifier.width(SpacingS))
        Text(text = text)
    }
}