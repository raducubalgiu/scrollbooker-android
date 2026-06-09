package com.example.scrollbooker.ui.search.sheets.services.steps

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.LastMinute
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun TimeIntervalCard(
    hasTime: Boolean,
    title: String,
    description: String? = null,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .heightIn(min = 65.dp)
            .widthIn(min = 130.dp)
            .border(
                width = if(isSelected) 2.dp else 1.dp,
                color = if(isSelected) LastMinute else Divider,
                shape = ShapeDefaults.Medium
            )
            .clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = null
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            style = titleMedium,
            fontWeight = FontWeight.SemiBold,
            text = title
        )
        if(hasTime && !description.isNullOrEmpty()) {
            Spacer(Modifier.height(SpacingXS))
            Text(
                text = description,
                color = Color.Gray
            )
        }
    }
}