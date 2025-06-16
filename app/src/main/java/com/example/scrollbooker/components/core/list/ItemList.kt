package com.example.scrollbooker.components.core.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.scrollbooker.R
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodySmall

@Composable
fun ItemList(
    modifier: Modifier = Modifier,
    headLine: String,
    headLineTextStyle: TextStyle = bodyLarge,
    supportingText: String = "",
    supportingTextStyle: TextStyle = bodySmall,
    leftIcon: Painter? = null,
    displayRightIcon: Boolean = true,
    onClick: () -> Unit
) {
    val isDarkMode = isSystemInDarkTheme()
    val background = if(isDarkMode) SurfaceBG else Background

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
                style = headLineTextStyle,
                text = headLine,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold
            )},
        supportingContent = {
            if(supportingText.isNotEmpty()) {
                Text(
                    style = supportingTextStyle,
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