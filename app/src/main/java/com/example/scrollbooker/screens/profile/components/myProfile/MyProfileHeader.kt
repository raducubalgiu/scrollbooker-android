package com.example.scrollbooker.screens.profile.components.myProfile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Segment
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.IconSizeXL
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun MyProfileHeader(
    username: String,
    onOpenBottomSheet: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier
            .size(50.dp)
            .clickable(
                onClick = onOpenBottomSheet,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
            contentAlignment = Alignment.Center
        ) {

        }
        Text(
            style = titleMedium,
            color = OnBackground,
            fontWeight = FontWeight.Bold,
            text = "@${username}"
        )
        Box(modifier = Modifier
            .size(50.dp)
            .clickable(
                onClick = onOpenBottomSheet,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Segment,
                contentDescription = null,
                tint = OnBackground,
                modifier = Modifier.size(IconSizeXL)
            )
        }
    }
}