package com.example.scrollbooker.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R

@Composable
fun ItemList(
    modifier: Modifier = Modifier,
    headLine: String,
    supportingText: String = "",
    leftIcon: Painter? = null,
    displayRightIcon: Boolean = true,
    onClick: () -> Unit
) {
    val isDarkMode = isSystemInDarkTheme()
    val background = if(isDarkMode) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background

    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = onClick,
            )
            .then(modifier),
        headlineContent = {
            Text(
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                text = headLine
            )},
        supportingContent = {
            if(supportingText.isNotEmpty()) {
                Text(
                    style = MaterialTheme.typography.labelMedium,
                    text = supportingText
                )
            }
        },
        leadingContent = {
            if(leftIcon != null) {
                Icon(
                    painter = leftIcon,
                    contentDescription = null,
                )
            }
        },
        trailingContent = {
            if(displayRightIcon) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = null,
                )
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = background
        )

    )
}