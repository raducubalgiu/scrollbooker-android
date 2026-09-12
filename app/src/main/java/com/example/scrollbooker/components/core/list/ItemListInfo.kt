package com.example.scrollbooker.components.core.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun ItemListInfo(
    modifier: Modifier = Modifier,
    headLine: String,
    headLineTextStyle: TextStyle = bodyLarge,
    description: String? = null,
    supportingText: String,
    supportingTextStyle: TextStyle = bodyMedium,
    leadingContent: @Composable (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .then(modifier),
        headlineContent = {
            Text(
                style = headLineTextStyle,
                text = headLine,
                fontWeight = FontWeight.SemiBold,
            )
        },
        supportingContent = description?.let {
            {
                Text(
                    style = bodyMedium,
                    text = it,
                    color = Color.Gray,
                )
            }
        },
        leadingContent = leadingContent,
        trailingContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    style = supportingTextStyle,
                    text = supportingText,
                    textAlign = TextAlign.End
                )
                Spacer(Modifier.width(SpacingS))
                Icon(
                    modifier = Modifier.size(17.dp),
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    tint = Color.Gray,
                )
            }
        },
        colors = ListItemColors(
            containerColor = Color.Transparent,
            headlineColor = OnBackground,
            overlineColor = Divider,
            supportingTextColor = OnBackground,
            trailingIconColor = OnBackground,
            disabledHeadlineColor = OnBackground,
            disabledLeadingIconColor = OnBackground,
            disabledTrailingIconColor = OnBackground,
            leadingIconColor = OnBackground
        )
    )
}