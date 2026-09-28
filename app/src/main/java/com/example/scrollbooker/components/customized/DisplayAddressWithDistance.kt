package com.example.scrollbooker.components.customized

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun DisplayAddressWithDistance(
    distanceKm: Float?,
    address: String
) {
    var isExpanded by remember { mutableStateOf(false) }

    val locationSummary = remember(distanceKm, address) {
        listOfNotNull(
            distanceKm?.let { "${"%.1f".format(it)}km" },
            address
        ).joinToString(" • ").takeIf { it.isNotBlank() }
    }

    Text(
        text = locationSummary ?: "",
        style = bodyMedium,
        color = Color.Gray,
        maxLines = if (isExpanded) Int.MAX_VALUE else 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .clickable(
                onClick = { isExpanded = !isExpanded },
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
            .animateContentSize()
    )
}