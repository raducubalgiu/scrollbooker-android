package com.example.scrollbooker.components.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun ItemListInfo(
    headLine: String,
    supportingText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .then(modifier),
        headlineContent = {
            Text(
                style = bodyMedium,
                text = headLine,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    style = bodyMedium,
                    text = supportingText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.width(SpacingS))
                Icon(
                    painter = painterResource(R.drawable.ic_next),
                    contentDescription = null
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