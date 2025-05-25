package com.example.scrollbooker.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun ItemSwitch(
    modifier: Modifier = Modifier,
    headLine: String,
    supportingText: String = "",
    leftIcon: Painter? = null,
    onClick: () -> Unit,
    checked: Boolean = true,
) {
    val isDarkMode = isSystemInDarkTheme()
    val background = if(isDarkMode) SurfaceBG else Background
    var checked by remember { mutableStateOf(checked) }

    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
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
            Switch(checked = checked,
                onCheckedChange = {
                    checked = it
                })
        },
        colors = ListItemDefaults.colors(
            containerColor = background
        )

    )
}