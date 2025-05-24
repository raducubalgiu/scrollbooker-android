package com.example.scrollbooker.feature.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.IconSizeXL
import com.example.scrollbooker.core.util.Dimens.SpacingM

@Composable
fun ProfileHeader(
    onOpenBottomSheet: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BasePadding, vertical = SpacingM),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clickable(
                    onClick = {},
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_prev),
                contentDescription = null
            )
        }
        Text(
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            text = "Radu Balgiu"
        )
        Box(modifier = Modifier
            .size(40.dp)
            .clickable(
                onClick = onOpenBottomSheet,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
            contentAlignment = Alignment.CenterEnd
        ) {

            Icon(
                painter = painterResource(R.drawable.ic_menu),
                contentDescription = null,
                modifier = Modifier.size(IconSizeXL)
            )
        }
    }
}